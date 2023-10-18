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
package io.ryos.cloud.mux;

import io.ryos.cloud.mux.validators.ValidationResult;

public class NoopMonitoringImpl implements Monitorable {

  @Override
  public void onValidationError(ValidationResult validationResult) {
  }

  @Override
  public void onRoute() {
  }

  @Override
  public void onSpill() {
  }
}