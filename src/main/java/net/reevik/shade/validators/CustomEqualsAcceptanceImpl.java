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

import static net.reevik.shade.validators.ValidatorFactory.newResult;

import java.util.function.BiPredicate;
import net.reevik.shade.Result;

public class CustomEqualsAcceptanceImpl<T> implements ResultValidator<T> {

  private final BiPredicate<Result<T>, Result<T>> predicate;

  public CustomEqualsAcceptanceImpl(BiPredicate<Result<T>, Result<T>> predicate) {
    this.predicate = predicate;
  }

  @Override
  public ValidationResult validate(Result<T> resultOnSideA, Result<T> resultOnSideB) {
    return newResult(predicate.test(resultOnSideA, resultOnSideB), String.format(
        "Equals check failed: The result from the A side: %s is not equal to the result from B side: %s",
        resultOnSideA, resultOnSideB));
  }
}
