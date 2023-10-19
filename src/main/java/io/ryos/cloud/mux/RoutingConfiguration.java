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

package io.ryos.cloud.mux;

import io.ryos.cloud.mux.validators.ResultValidator;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class RoutingConfiguration<T> {

  private final SideCommand<T> sideACommand;
  private final SideCommand<T> sideBCommand;
  private final ExecutorService executorService;
  private final ResultValidator<T> validator;
  private final RoutingMode routingMode;
  private final RoutingCriterion routingCriterion;
  private final Monitorable monitorable;

  public RoutingConfiguration(SideCommand<T> sideACommand, SideCommand<T> sideBCommand,
      ExecutorService executorService, ResultValidator<T> validator, RoutingMode routingMode,
      RoutingCriterion routingCriterion, Monitorable monitorable) {
    this.sideACommand = sideACommand;
    this.sideBCommand = sideBCommand;
    this.executorService = executorService;
    this.validator = validator;
    this.routingMode = routingMode;
    this.routingCriterion = routingCriterion;
    this.monitorable = monitorable;
  }

  static class Builder<T> {

    private SideCommand<T> sideACommand;
    private SideCommand<T> sideBCommand;
    private ExecutorService executorService;
    private ResultValidator<T> validator;
    private RoutingMode routingMode;
    private RoutingCriterion routingCriterion;
    private Monitorable monitorable = new NoopMonitoringImpl();

    public static <T> Builder<T> create() {
      return new Builder<>();
    }

    public Builder<T> withSideA(Supplier<T> supplier) {
      this.sideACommand = new SideCommand<T>(supplier);
      return this;
    }

    public Builder<T> withSideB(Supplier<T> supplier) {
      this.sideBCommand = new SideCommand<T>(supplier);
      return this;
    }

    public Builder<T> withExecutorService(ExecutorService executorService) {
      this.executorService = executorService;
      return this;
    }

    public Builder<T> withResultValidator(ResultValidator<T> validator) {
      this.validator = validator;
      return this;
    }

    public Builder<T> withRoutingMode(RoutingMode routingMode) {
      this.routingMode = routingMode;
      return this;
    }

    public Builder<T> withRoutingCriterion(RoutingCriterion routingCriterion) {
      this.routingCriterion = routingCriterion;
      return this;
    }

    public Builder<T> withMonitorable(Monitorable monitorable) {
      this.monitorable = monitorable;
      return this;
    }

    public RoutingConfiguration<T> build() {
      return new RoutingConfiguration<>(sideACommand, sideBCommand, executorService, validator,
          routingMode, routingCriterion, monitorable);
    }
  }

  public SideCommand<T> getSideACommand() {
    return sideACommand;
  }

  public SideCommand<T> getSideBCommand() {
    return sideBCommand;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }

  public ResultValidator<T> getValidator() {
    return validator;
  }

  public RoutingMode getRoutingMode() {
    return routingMode;
  }

  public RoutingCriterion getRoutingCriterion() {
    return routingCriterion;
  }

  public Monitorable getMonitorable() {
    return monitorable;
  }
}
