package io.ryos.cloud.mux.validators;

import java.util.Objects;

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

public class ValidationResult {
  private final boolean passed;
  private final String description;

  public ValidationResult(boolean passed, String description) {
    this.passed = passed;
    this.description = description;
  }

  public boolean isPassed() {
    return passed;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationResult that = (ValidationResult) o;
    return passed == that.passed && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(passed, description);
  }
}
