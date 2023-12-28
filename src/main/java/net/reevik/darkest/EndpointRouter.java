/*
 * Copyright (c) 2023 Erhan Bagdemir. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.reevik.darkest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EndpointRouter<T> {

  private final RoutingConfiguration<T> config;

  public EndpointRouter(RoutingConfiguration<T> config) {
    this.config = config;
  }

  public T route() throws Exception {
    Result<T> result;
    var sideBCommand = config.getSideBCommand();
    var sideACommand = config.getSideACommand();
    result = switch (config.getRoutingMode()) {
      case B_SIDE -> sideBCommand.run();
      case ROLL_OUT -> routeIfCanRoute(sideBCommand, sideACommand);
      case SHADOW_MODE_ACTIVE -> evaluateAndReturn(sideBCommand, sideACommand);
      case SHADOW_MODE_PASSIVE -> evaluate(sideBCommand, sideACommand);
      default -> sideACommand.run();
    };
    return result.getOrThrow();
  }

  private Result<T> evaluateAndReturn(SideCommand<T> sideBCommand, SideCommand<T> sideACommand)
      throws InterruptedException, ExecutionException {
    var resultA = sideACommand.run();
    var result = evalAB(sideBCommand, resultA);
    return result.get();
  }

  private Future<Result<T>> evalAB(SideCommand<T> sideBCommand, Result<T> resultA) {
    var validator = config.getValidator();
    var monitorable = config.getMonitorable();
    return config.getExecutorService().submit(() -> {
      var resultB = sideBCommand.run();
      var validationResult = validator.validate(resultA, resultB);
      if (validationResult.isPassed()) {
        monitorable.onRoute();
        return resultB;
      }
      monitorable.onValidationError(validationResult);
      return resultA;
    });
  }

  private Result<T> evaluate(SideCommand<T> sideBCommand, SideCommand<T> sideACommand) {
    var resultA = sideACommand.run();
    evalAB(sideBCommand, sideACommand.run());
    return resultA;
  }

  private Result<T> routeIfCanRoute(SideCommand<T> sideBCommand, SideCommand<T> sideACommand) {
    if (canRoute()) {
      return sideBCommand.run();
    }
    return sideACommand.run();
  }

  private boolean canRoute() {
    return config.getRoutingCriterion().canRoute();
  }
}
