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

package net.reevik.darkest.validators;

import java.util.Collections;

/**
 * Validator factory is a factory implementation for {@link ResultValidator}.
 */
public class ValidatorFactory {

  /**
   * The validator assures that the results from both endpoints equal.
   *
   * @param <T> Generic type of the result object.
   * @return {@link ResultValidator} instance.
   */
  public static <T> ResultValidator<T> mustEqual() {
    return new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>()));
  }

  /**
   * Factory method for {@link ValidationResult}.
   *
   * @param passed           Whether the validation succeeded.
   * @param errorDescription If the validation fails, error description is required.
   * @return {@link ValidationResult} instance.
   */
  public static ValidationResult newResult(boolean passed, String errorDescription) {
    return new ValidationResult(passed, !passed ? errorDescription : "");
  }
}

