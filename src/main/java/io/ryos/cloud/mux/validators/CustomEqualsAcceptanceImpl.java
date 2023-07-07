package io.ryos.cloud.mux.validators;

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

import io.ryos.cloud.mux.Result;
import java.util.function.BiPredicate;

public class CustomEqualsAcceptanceImpl<T> implements AcceptanceCriterion<T> {

  private final BiPredicate<Result<T>, Result<T>> predicate;

  public CustomEqualsAcceptanceImpl(BiPredicate<Result<T>, Result<T>> predicate) {
    this.predicate = predicate;
  }

  @Override
  public boolean check(Result<T> resultOnSideA, Result<T> resultOnSideB) {
    return predicate.test(resultOnSideA, resultOnSideB);
  }
}
