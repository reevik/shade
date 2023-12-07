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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Criterion which enables routing in case the number of requests reach a pre-configured threshold.
 */
public class CountingCriterion implements RoutingCriterion {

  private int counter;
  private final int mod;

  private final ReentrantLock lock = new ReentrantLock();

  public CountingCriterion(int mod) {
    this.mod = mod;
  }

  @Override
  public boolean canRoute() {
    try {
      if (lock.tryLock(5, TimeUnit.SECONDS)) {
        boolean canRoute = ++counter % mod == 0;
        if (canRoute) {
          counter = 0;
          return true;
        }
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
    return false;
  }
}
