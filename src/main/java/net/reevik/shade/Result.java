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

/**
 * A wrapper for the result object from the endpoint call.
 *
 * @param <T> Actual result type.
 */
public interface Result<T> {

  /**
   * Returns the result object if the endpoint call succeeded, or throws an exception if otherwise.
   *
   * @return The result object from the endpoint.
   * @throws Exception If the call failed.
   */
  T getOrThrow() throws Exception;

  /**
   * Endpoint call duration.
   *
   * @return {@link Duration} instance.
   */
  Duration duration();

  /**
   * If the call succeeds, returns true.
   *
   * @return f the call succeeds, returns true.
   */
  boolean isSucceeded();
}
