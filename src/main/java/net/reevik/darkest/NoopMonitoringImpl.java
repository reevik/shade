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

package net.reevik.darkest;

import net.reevik.darkest.validators.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopMonitoringImpl implements Monitorable {

  private static final Logger logger = LoggerFactory.getLogger(NoopMonitoringImpl.class);

  @Override
  public void onValidationError(ValidationResult validationResult) {
    logger.error("Compatibility check=failed, reason={}", validationResult.description());
  }

  @Override
  public void onValidationSuccess(ValidationResult validationResult) {
    logger.info("Compatibility check=passed");
  }

  @Override
  public void onRoute() {
    logger.info("The request routed to the new side.");
  }

  @Override
  public void onStay() {
    logger.info("The request routed to the old side.");
  }
}
