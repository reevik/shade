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

package net.reevik.mux.validators;

import static net.reevik.mux.validators.ValidatorFactory.newResult;

import net.reevik.mux.Result;

public class EqualsValidatorImpl<T> implements ResultValidator<T> {

  @Override
  public ValidationResult validate(Result<T> resultA, Result<T> resultB) {
    return newResult(resultA.equals(resultB), String.format(
        "Equals check failed: The result from the A side: %s is not equal to the result from B side: %s",
        resultA, resultB));
  }
}
