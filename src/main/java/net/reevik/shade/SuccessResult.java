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

import java.time.Duration;
import java.util.Objects;

public record SuccessResult<T>(T result, Duration duration) implements Result<T> {

  @Override
  public T getOrThrow() throws Exception {
    return result;
  }

  @Override
  public boolean isSucceeded() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SuccessResult<?> that = (SuccessResult<?>) o;
    return Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result);
  }

  @Override
  public String toString() {
    return "SuccessResult{" +
        "result=" + result +
        ", duration=" + duration.toMillis() + " millis" +
        '}';
  }
}
