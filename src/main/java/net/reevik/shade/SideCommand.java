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
import java.time.Instant;
import java.util.concurrent.Callable;

public class SideCommand<T> {

  private final Callable<T> supplier;

  protected SideCommand(Callable<T> supplier) {
    this.supplier = supplier;
  }

  protected Result<T> run() {
    Instant start = Instant.now();
    try {
      return new SuccessResult<>(supplier.call(), Duration.between(start, Instant.now()));
    } catch (Exception e) {
      return new ErrorResult<>(e, Duration.between(start, Instant.now()));
    }
  }
}
