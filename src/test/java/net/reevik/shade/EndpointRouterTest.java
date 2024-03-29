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

import static net.reevik.shade.MonitorableAssertionFactory.expectSuccess;
import static net.reevik.shade.validators.ValidatorFactory.mustEqual;
import static net.reevik.shade.validators.ValidatorFactory.mustPerformSimilar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.Collections;
import net.reevik.shade.RoutingConfiguration.Builder;
import net.reevik.shade.criteria.CountingCondition;
import net.reevik.shade.validators.EqualsValidatorImpl;
import net.reevik.shade.validators.ResultValidator;
import net.reevik.shade.validators.ResultValidatorImpl;
import net.reevik.shade.validators.ValidationResult;
import net.reevik.shade.validators.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EndpointRouterTest {

  private final ResultValidator<String> alwaysTrueValidator =
      (resultA, resultB) -> ValidatorFactory.newResult(true, "N/A");

  @Test
  public void testNonRouting() throws Exception {
    CountingCondition countingCriterion = new CountingCondition(1);
    ResultValidator<String> mustEqual = spy(mustEqual());
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "A")
        .withSideB(EndpointRouterTest::assertNotCalled)
        .withResultValidator(mustEqual)
        .withRoutingCriterion(countingCriterion)
        .withRoutingMode(RoutingMode.A_SIDE)
        .build();

    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    String result = router.route();
    assertThat(result).isEqualTo("A");
    verify(mustEqual, never()).validate(any(), any());
  }

  private static String assertNotCalled() {
    throw new AssertionError();
  }

  @Test
  public void testAlwaysRouting() throws Exception {
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(EndpointRouterTest::assertNotCalled)
        .withSideB(() -> "B")
        .withResultValidator(mustEqual())
        .withRoutingCriterion(countingCriterion)
        .withRoutingMode(RoutingMode.B_SIDE)
        .build();

    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    String result = router.route();
    assertThat(result).isEqualTo("B");
  }

  @Test
  public void shadowTestingPassing() throws Exception {
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "abc").withSideB(() -> "abc").withResultValidator(mustEqual())
        .withRoutingCriterion(countingCriterion).withMonitorable(expectSuccess())
        .withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE).build();

    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo("abc");
  }

  @Test
  public void shadowTestingPassingWithResultFromBSide() throws Exception {
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "abc").withSideB(() -> "dbc").withResultValidator(alwaysTrueValidator)
        .withRoutingCriterion(countingCriterion).withMonitorable(expectSuccess())
        .withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE).build();

    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo("dbc");
  }

  @Test
  public void shadowTestingPassingWithResultFromASide() throws Exception {
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "abc").withSideB(() -> "dbc").withResultValidator(alwaysTrueValidator)
        .withRoutingCriterion(countingCriterion).withMonitorable(expectSuccess())
        .withRoutingMode(RoutingMode.SHADOW_MODE_PASSIVE).build();

    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo("abc");
  }

  @Test
  public void shadowTestingValidationFailure() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>())));
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "abc").withSideB(() -> "abd").withResultValidator(validator)
        .withRoutingCriterion(countingCriterion).withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE)
        .build();

    ValidatorResultCapture<ValidationResult> capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo("abc");
    assertThat(capture.getResult().passed()).isFalse();
  }

  @Test
  public void shadowTestingValidationPassesForBothNull() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>())));
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> null).withSideB(() -> null).withResultValidator(validator)
        .withRoutingCriterion(countingCriterion).withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE)
        .build();

    ValidatorResultCapture<ValidationResult> capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo(null);
    assertThat(capture.getResult().passed()).isTrue();
  }

  @Test
  public void shadowTestingValidationFailsWithNull() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>())));
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "abc").withSideB(() -> null).withResultValidator(validator)
        .withRoutingCriterion(countingCriterion).withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE)
        .build();

    ValidatorResultCapture<ValidationResult> capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo("abc");
    assertThat(capture.getResult().passed()).isFalse();
  }

  @Test
  public void shadowTestingExecutionFailsWithExceptionHandledGracefully() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>())));
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create()
        .withSideA(() -> "abc").withSideB(() -> {
          throw new Exception("test");
        }).withResultValidator(validator).withRoutingCriterion(countingCriterion)
        .withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE).build();

    ValidatorResultCapture<ValidationResult> capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThat(router.route()).isEqualTo("abc");
    assertThat(capture.getResult().passed()).isFalse();
  }

  @Test
  public void shadowTestingBothExecutionFailsWithExceptionAndEquals() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>())));
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create().withSideA(() -> {
          throw new Exception("test");
        }).withSideB(() -> {
          throw new Exception("test");
        }).withResultValidator(validator).withRoutingCriterion(countingCriterion)
        .withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE).build();
    ValidatorResultCapture<ValidationResult> capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThatExceptionOfType(Exception.class).isThrownBy(router::route);
    assertThat(capture.getResult().passed()).isTrue();
  }

  @Test
  public void shadowTestingBothExecutionFailsWithExceptionAndNotEqual() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(Collections.singletonList(new EqualsValidatorImpl<>())));
    CountingCondition countingCriterion = new CountingCondition(1);
    RoutingConfiguration<String> routingConfiguration = Builder.<String>create().withSideA(() -> {
          throw new Exception("test");
        }).withSideB(() -> {
          throw new IllegalArgumentException("test");
        }).withResultValidator(validator).withRoutingCriterion(countingCriterion)
        .withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE).build();
    ValidatorResultCapture<ValidationResult> capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    assertThatExceptionOfType(Exception.class).isThrownBy(router::route);
    assertThat(capture.getResult().passed()).isFalse();
  }

  @Test
  public void shadowTestingPerformanceValidationFails() throws Exception {
    ResultValidatorImpl<String> validator = spy(
        new ResultValidatorImpl<>(
            Collections.singletonList(mustPerformSimilar(Duration.ofMillis(5)))));
    var countingCriterion = new CountingCondition(1);
    var routingConfiguration = Builder.<String>create().withSideA(() -> {
          throw new Exception("test");
        }).withSideB(() -> {
          Thread.sleep(100);
          throw new Exception("test");
        }).withResultValidator(validator).withRoutingCriterion(countingCriterion)
        .withRoutingMode(RoutingMode.SHADOW_MODE_ACTIVE).build();

    var capture = new ValidatorResultCapture<>();
    doAnswer(capture).when(validator).validate(any(), any());
    var router = new EndpointRouter<>(routingConfiguration);
    assertThatExceptionOfType(Exception.class).isThrownBy(router::route);
    assertThat(capture.getResult().passed()).isFalse();
  }
}
