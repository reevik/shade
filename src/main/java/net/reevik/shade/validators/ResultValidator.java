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

package net.reevik.shade.validators;

import net.reevik.shade.Result;

/**
 * The response objects from both endpoints need to be compared to evaluate if they are compatible
 * so that the endpoint switch can be performed. {@link ResultValidator} instances are used to
 * validate the responses of the endpoints.
 *
 * @param <T> The actual type of the endpoint response.
 */
public interface ResultValidator<T> {

  /**
   * Validates if the result from the B side is compatible with the one from the A side.
   *
   * @param resultA {@link Result} instance from the A side.
   * @param resultB {@link Result} instance from the B side.
   * @return {@link ValidationResult} instance.
   */
  ValidationResult validate(Result<T> resultA, Result<T> resultB);
}
