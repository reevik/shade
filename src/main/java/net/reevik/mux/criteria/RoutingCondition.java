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

package net.reevik.mux.criteria;

/**
 * Routing condition determines when an incoming request needs to be routed to the new endpoint. In
 * different scenarios you may want to route different number of requests. For example, the
 * implementation may choose to route a specific percentage of incoming requests to the new side, or
 * in a cut-over scenario, all requests need to be directed.
 */
public interface RoutingCondition {

  /**
   * Returns @code true} if the request should be routed.
   *
   * @return {@code true} if the request should be routed.
   */
  boolean canRoute();
}
