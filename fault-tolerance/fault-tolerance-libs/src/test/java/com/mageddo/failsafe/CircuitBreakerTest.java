package com.mageddo.failsafe;

import java.io.UncheckedIOException;
import java.time.Duration;

import com.mageddo.concurrency.Threads;
import com.mageddo.supporting.sandbox.Result;
import com.mageddo.supporting.sandbox.Stats;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import dev.failsafe.CircuitBreaker;
import dev.failsafe.CircuitBreaker.State;
import static com.mageddo.supporting.sandbox.failsafe.FailSafeCircuitBreakerSandBox.runError;
import static com.mageddo.supporting.sandbox.failsafe.FailSafeCircuitBreakerSandBox.runSuccess;
import static com.mageddo.supporting.sandbox.failsafe.FailSafeCircuitBreakerSandBox.testCircuitOnError;
import static com.mageddo.supporting.sandbox.failsafe.FailSafeCircuitBreakerSandBox.testCircuitOnSuccess;
import static com.mageddo.supporting.sandbox.failsafe.StatsCalculator.calcStats;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * https://failsafe.dev/circuit-breaker/
 */
public class CircuitBreakerTest {

  @Test
  void mustThrowOpenCircuitExceptionWhenTryingToUseOpenCircuit() {

    final var stats = new Stats();
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(2)
        .withSuccessThreshold(2)
        .withDelay(Duration.ofMillis(500))
        .build();

    assertEquals(State.CLOSED, circuit.getState());

    calcStats(stats, () -> runError(circuit));
    assertEquals(State.CLOSED, circuit.getState());

    calcStats(stats, () -> runError(circuit));
    assertEquals(State.OPEN, circuit.getState());

    calcStats(stats, () -> runSuccess(circuit));
    assertEquals(State.OPEN, circuit.getState());

    assertEquals(2, stats.error);
    assertEquals(1, stats.openCircuit);
    assertEquals(0, stats.success);
  }

  @Test
  void mustOpenAfterConfiguredFailures() {

    final var stats = new Stats();
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(2)
        .withSuccessThreshold(2)
        .withDelay(Duration.ofMillis(500))
        .build();

    assertEquals(State.CLOSED, circuit.getState());

    calcStats(stats, () -> runError(circuit));
    assertEquals(State.CLOSED, circuit.getState());

    calcStats(stats, () -> runError(circuit));
    assertEquals(State.OPEN, circuit.getState());

  }

  @Test
  void mustHalfOpenAfterRunATaskWithSuccessAfterTheConfiguredTime() {

    final var stats = new Stats();
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(2)
        .withSuccessThreshold(2)
        .withDelay(Duration.ofMillis(300))
        .build();

    circuit.open();
    assertEquals(State.OPEN, circuit.getState());

    Threads.sleep(500);

    calcStats(stats, () -> runSuccess(circuit));
    assertEquals(State.HALF_OPEN, circuit.getState());

  }

  @Test
  void waitDelayTimeAndDontTryToExecuteTaskWontChangeTheState() {

    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(2)
        .withSuccessThreshold(2)
        .withDelay(Duration.ofMillis(300))
        .build();

    circuit.open();
    assertEquals(State.OPEN, circuit.getState());

    Threads.sleep(500);

    assertEquals(State.OPEN, circuit.getState());

  }

  @Test
  void mustOpenThenHalfOpenTheCircuit() {

    final var stats = new Stats();

    final var breaker = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(3, 5)
        .withSuccessThreshold(5)
        .withDelay(Duration.ofMillis(500))
        .build();

    calcStats(stats, () -> runSuccess(breaker));
    calcStats(stats, () -> runSuccess(breaker));
    calcStats(stats, () -> runSuccess(breaker));

    calcStats(stats, () -> runError(breaker));
    calcStats(stats, () -> runError(breaker));
    calcStats(stats, () -> runError(breaker));
    calcStats(stats, () -> runError(breaker));

    calcStats(stats, () -> runSuccess(breaker));

    Threads.sleep(1000);

    calcStats(stats, () -> runSuccess(breaker));
    calcStats(stats, () -> runSuccess(breaker));

    assertTrue(breaker.isHalfOpen());
    assertFalse(breaker.isOpen());
    assertFalse(breaker.isClosed());
    assertEquals(5, stats.success);
    assertEquals(3, stats.error);
    assertEquals(2, stats.openCircuit);
  }

  @Test
  void mustReopenCircuitAfterTheThresholdBetMet() {

    final var ignoredInThisUseCase = 100;
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureRateThreshold(ignoredInThisUseCase, 100, Duration.ofMillis(30))
        .withDelay(Duration.ofMillis(80))
        .build();

    circuit.halfOpen();


    for (int i = 0; i < 3; i++) {

      testCircuitOnError(Result.ERROR, State.HALF_OPEN, circuit, 99);
      testCircuitOnError(Result.ERROR, State.OPEN, circuit, 1);
      testCircuitOnError(Result.CIRCUIT_OPEN, State.OPEN, circuit, 35);

      Threads.sleep(81);
    }

  }

  /**
   * withSuccessThreshold sobrepoe o withFailureThreshold
   * <p>
   * O circuit fica aberto pelo wityDelay,
   * depois fica meio aberto pela quantidade  successThresholdingCapacity - successThreshold,
   * depois fica aberto denovo pelo withDelay
   */
  @Test
  void halfOpenBehaviorWhenUsingSuccessThresholdAndCapacity() {

    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureThreshold(2, 100) // is ignored as withSuccessThreshold is set
        .withSuccessThreshold(3, 100)
        .withDelay(Duration.ofMillis(80))
        .build();

    circuit.halfOpen();


    for (int i = 0; i < 3; i++) {

      testCircuitOnError(Result.ERROR, State.HALF_OPEN, circuit, 97);
      testCircuitOnError(Result.ERROR, State.OPEN, circuit, 1);
      testCircuitOnError(Result.CIRCUIT_OPEN, State.OPEN, circuit, 100);

      Threads.sleep(100);
    }


  }

  /**
   * This scenario doesn't makes sense for me, failureThresholdingPeriod is not considered
   */
  @Test
  void mustReopenCircuitAfterThePERIODBetMet() {

    final var ignoredInThisUseCase = 100;
    final var failureThresholdingPeriod = Duration.ofMillis(100);
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureRateThreshold(ignoredInThisUseCase, 100, failureThresholdingPeriod)
        .withDelay(Duration.ofMillis(80))
        .build();

    circuit.halfOpen();


    for (int i = 0; i < 3; i++) {

      testCircuitOnError(Result.ERROR, State.HALF_OPEN, circuit, 10);
      Threads.sleep(10);
      testCircuitOnError(Result.ERROR, State.HALF_OPEN, circuit, 10);
      Threads.sleep(100);

      testCircuitOnError(Result.ERROR, State.HALF_OPEN, circuit, 79);

      testCircuitOnError(Result.ERROR, State.OPEN, circuit, 1);
      testCircuitOnError(Result.CIRCUIT_OPEN, State.OPEN, circuit, 35);

      Threads.sleep(81);
    }
  }

  @Test
  void whenFailureExecutionThresholdIsMetMustCloseCircuit() {

    final var ignoredInThisUseCase = 10;
    final var failureThresholdingPeriod = Duration.ofMillis(100);
    final var failureExecutionThreshold = 100;
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureRateThreshold(ignoredInThisUseCase, failureExecutionThreshold,
            failureThresholdingPeriod)
        .withDelay(Duration.ofMillis(80))
        .build();

    for (int i = 0; i < 2; i++) {

      circuit.halfOpen();
      testCircuitOnSuccess(Result.SUCCESS, State.HALF_OPEN, circuit, 99);
      testCircuitOnSuccess(Result.SUCCESS, State.CLOSED, circuit, 1);

    }
  }

  @Test
  void whenFailureThresholdingPeriodIsMetMustCloseCircuitButItDontWork() {

    final var ignoredInThisUseCase = 10;
    final var failureThresholdingPeriod = Duration.ofMillis(100);
    final var failureExecutionThreshold = 100;
    final var circuit = CircuitBreaker.<String>builder()
        .handle(UncheckedIOException.class)
        .withFailureRateThreshold(ignoredInThisUseCase, failureExecutionThreshold,
            failureThresholdingPeriod)
        .withDelay(Duration.ofMillis(80))
        .build();


    circuit.halfOpen();

    for (int i = 0; i < 1; i++) {

      testCircuitOnSuccess(Result.SUCCESS, State.HALF_OPEN, circuit, 90);

      Threads.sleep(120);

      assertThrows(
          AssertionFailedError.class,
          () -> {
            testCircuitOnSuccess(Result.SUCCESS, State.CLOSED, circuit, 1);
          }
      );

    }
  }

}
