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

package net.reevik.shade;

import net.reevik.shade.validators.ValidationResult;

/**
 * Implementations of {@link Monitorable} will get called, whenever a routing event is triggered.
 * You can use {@link Monitorable} for your own purpose, for instance, to create metrics for each
 * event type and monitor the roll-out.
 */
public interface Monitorable {

  /**
   * The method will get triggered on validation error.
   *
   * @param validationResult An instance contains information about validation.
   */
  void onValidationError(ValidationResult validationResult);

  /**
   * The method will get triggered on validation success.
   *
   * @param validationResult An instance contains information about validation.
   */
  void onValidationSuccess(ValidationResult validationResult);

  /**
   * The method will get triggered whenever the request is routed to the new endpoint.
   */
  void onRoute();

  /**
   * The method will get triggered whenever the request is routed to the old endpoint.
   */
  void onStay();
}
