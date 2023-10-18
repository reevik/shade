package io.ryos.cloud.mux;

/*******************************************************************************
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
 ******************************************************************************/

import io.ryos.cloud.mux.validators.ResultValidator;
import io.ryos.cloud.mux.validators.ValidationResult;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EndpointRouter<T> {

  private final RoutingConfiguration<T> config;

  public EndpointRouter(final RoutingConfiguration<T> config) {
    this.config = config;
  }

  public T route() throws Exception {
    Result<T> result;
    SideCommand<T> sideBCommand = config.getSideBCommand();
    SideCommand<T> sideACommand = config.getSideACommand();
    switch (config.getRoutingMode()) {
      case B_SIDE:
        result = sideBCommand.run();
        break;
      case ROLL_OUT:
        result = rollOut(sideBCommand, sideACommand);
        break;
      case SHADOW_MODE_ACTIVE:
        result = shadowTestAndReturn(sideBCommand, sideACommand);
        break;
      case SHADOW_MODE_PASSIVE:
        result = shadowTest(sideBCommand, sideACommand);
        break;
      case A_SIDE:
      default:
        result = sideACommand.run();
    }
    return result.getOrThrow();
  }

  private Result<T> shadowTestAndReturn(SideCommand<T> sideBCommand, SideCommand<T> sideACommand)
      throws InterruptedException, ExecutionException {
    Result<T> resultA = sideACommand.run();
    Future<Result<T>> result = getResult(sideBCommand, resultA);
    return result.get();
  }

  private Future<Result<T>> getResult(SideCommand<T> sideBCommand, Result<T> resultA) {
    ResultValidator<T> validator = config.getValidator();
    Monitorable monitorable = config.getMonitorable();

    return config.getExecutorService().submit(() -> {
      Result<T> resultB = sideBCommand.run();
      ValidationResult validationResult = validator.validate(resultA, resultB);
      if (validationResult.isPassed()) {
        monitorable.onRoute();
        return resultB;
      }
      monitorable.onValidationError(validationResult);
      return resultA;
    });
  }

  private Result<T> shadowTest(SideCommand<T> sideBCommand, SideCommand<T> sideACommand) {
    Result<T> resultA = sideACommand.run();
    getResult(sideBCommand, sideACommand.run());
    return resultA;
  }

  private Result<T> rollOut(SideCommand<T> sideBCommand, SideCommand<T> sideACommand) {
    Result<T> result;
    if (canRoute()) {
      result = sideBCommand.run();
    } else {
      result = sideACommand.run();
    }
    return result;
  }

  private boolean canRoute() {
    return config.getRoutingCriterion().canRoute();
  }
}
