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

/**
 * Routing mode dictates the current behaviour of the request routing.
 */
public enum RoutingMode {

  /**
   * A side only. All requests must be redirected to the old endpoint.
   */
  A_SIDE,

  /**
   * B side only. All requests must be redirected to the new endpoint.
   */
  B_SIDE,

  /**
   * Shadow mode is active in passive mode. Both endpoints will be simultaneously called and
   * response objects will be analysed. The result of the analysis will be reported through
   * monitoring integration. Nevertheless, the response from the old endpoint will be returned.
   */
  SHADOW_MODE_PASSIVE,

  /**
   * Shadow mode is active in active mode. Both endpoints will be simultaneously called and response
   * objects will be analysed. The result of the analysis will be reported through monitoring
   * integration. Finally, the response from the new endpoint will be returned.
   */
  SHADOW_MODE_ACTIVE,

  /**
   * Enables the roll-out mode. Depending on the routing condition, the request will be routed
   * either to the old or new endpoint.
   */
  ROLL_OUT
}
