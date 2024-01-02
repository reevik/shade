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

import static net.reevik.darkest.validators.ValidatorFactory.newResult;

import java.time.Duration;
import net.reevik.darkest.Result;

public class PerformanceValidatorImpl<T> implements ResultValidator<T> {
  private final Duration maxDeviance;

  public PerformanceValidatorImpl(Duration maxDeviance) {
    this.maxDeviance = maxDeviance;
  }

  @Override
  public ValidationResult validate(Result<T> resultASide, Result<T> resultBSide) {
    var diffDuration = resultBSide.duration().minus(resultASide.duration());
    return newResult(maxDeviance.minus(diffDuration).toMillis() > 0, String.format(
        "Performance check failed: The response time of the B Side deviates by %d millis.",
        diffDuration.toMillis()));
  }
}
