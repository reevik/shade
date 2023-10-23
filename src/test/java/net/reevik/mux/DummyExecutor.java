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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DummyExecutor implements ExecutorService {
  private boolean shutdown;
  private boolean terminated;

  public DummyExecutor() {
    shutdown = false;
    terminated = false;
  }

  @Override
  public void shutdown() {
    shutdown = true;
    terminated = true;
  }

  @Override
  public List<Runnable> shutdownNow() {
    shutdown();
    return Collections.emptyList();
  }

  @Override
  public boolean isShutdown() {
    return shutdown;
  }

  @Override
  public boolean isTerminated() {
    return terminated;
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) {
    return isTerminated();
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return execute(task);
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    task.run();
    return CompletableFuture.completedFuture(result);
  }

  @Override
  public Future<?> submit(Runnable task) {
    task.run();
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
    return tasks.stream().map(this::execute).collect(Collectors.toList());
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout,
      TimeUnit unit) {
    return invokeAll(tasks);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
    return null;
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
    return null;
  }

  @Override
  public void execute(Runnable command) {
    command.run();
  }

  private <T> Future<T> execute(Callable<T> task) {
    try {
      return CompletableFuture.completedFuture(task.call());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
