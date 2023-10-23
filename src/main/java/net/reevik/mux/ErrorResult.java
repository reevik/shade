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

package net.reevik.mux;

import java.time.Duration;
import java.util.Objects;

public class ErrorResult<T> implements Result<T> {

  private final Exception e;
  private final Duration duration;

  public ErrorResult(Exception e, Duration duration) {
    this.e = e;
    this.duration = duration;
  }

  public Exception getError() {
    return e;
  }

  @Override
  public T getOrThrow() throws Exception {
    throw e;
  }

  @Override
  public Duration getDuration() {
    return duration;
  }

  @Override
  public boolean isSucceeded() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResult<?> that = (ErrorResult<?>) o;
    return Objects.equals(e, that.e);
  }

  @Override
  public int hashCode() {
    return Objects.hash(e);
  }
}
