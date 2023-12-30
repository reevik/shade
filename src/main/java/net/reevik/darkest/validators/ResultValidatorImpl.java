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

import java.util.List;
import net.reevik.darkest.Result;

public class ResultValidatorImpl<T> implements ResultValidator<T> {

  private final List<ResultValidator<T>> acceptanceCriteria;

  public ResultValidatorImpl(List<ResultValidator<T>> acceptanceCriteria) {
    this.acceptanceCriteria = acceptanceCriteria;
  }

  public ValidationResult validate(Result<T> resultA, Result<T> resultB) {
    return acceptanceCriteria.stream()
        .map(criterion -> criterion.validate(resultA, resultB))
        .reduce((validationResultA, validationResultB) -> ValidatorFactory.newResult(
            validationResultA.passed() && validationResultB.passed(),
            validationResultA.description().concat(validationResultB.description())))
        .orElse(ValidatorFactory.newResult(false, ""));
  }
}
