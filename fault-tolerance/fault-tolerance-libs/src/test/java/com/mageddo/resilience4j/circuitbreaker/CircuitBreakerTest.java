package com.mageddo.resilience4j.circuitbreaker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

import com.mageddo.concurrency.Threads;
import com.mageddo.failsafe.Result;

import org.junit.jupiter.api.Test;

import static com.mageddo.resilience4j.supporting.Reslience4jTestUtils.testCircuitOnSuccess;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CircuitBreakerTest {

  @Test
  void whenFifthPercentOfMinimumCallsIsMetMustOpenTheCircuitAsItIsGetting100percentErrorRate() {

    final var minimumNumberOfCalls = 10;
    final var minimumNumberOfCallsBeforeCalculateThreshold = minimumNumberOfCalls / 2;
    final var circuit = CircuitBreaker.of(
        "defaultCircuitBreaker",
        CircuitBreakerConfig
            .custom()
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .failureRateThreshold(90.0f)
            .minimumNumberOfCalls(minimumNumberOfCalls)
            .recordExceptions(UncheckedIOException.class)
            .build()
    );

    final var decoratedSupplier = CircuitBreaker.decorateSupplier(
        circuit,
        () -> {
          throw new UncheckedIOException(new IOException("an error"));
        }
    );

    assertEquals(State.CLOSED, circuit.getState());
    for (int i = 0; i < minimumNumberOfCallsBeforeCalculateThreshold; i++) {
      assertThrows(UncheckedIOException.class, () -> circuit.executeSupplier(decoratedSupplier));
    }
    assertEquals(State.OPEN, circuit.getState());

  }

  @Test
  void permittedNumberOfCallsInHalfOpenStateWillOverrideMinimumNumberOfCalls() {

    final var circuit = CircuitBreaker.of(
        "defaultCircuitBreaker",
        CircuitBreakerConfig
            .custom()
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .minimumNumberOfCalls(10)
            .failureRateThreshold(10.0f)
            .permittedNumberOfCallsInHalfOpenState(5)
            .maxWaitDurationInHalfOpenState(Duration.ofMillis(100))
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .recordExceptions(UncheckedIOException.class)
            .build()
    );

    circuit.transitionToOpenState();
    circuit.transitionToHalfOpenState();

    testCircuitOnSuccess(Result.SUCCESS, State.HALF_OPEN, circuit, 4);
    testCircuitOnSuccess(Result.SUCCESS, State.CLOSED, circuit, 1);

  }

  @Test
  void mustReopenTheCircuitWhenMinimumNumberOfCallsIsNotMetInMaxWaitDurationInHalfOpenState() {

    final var circuit = CircuitBreaker.of(
        "defaultCircuitBreaker",
        CircuitBreakerConfig
            .custom()
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .minimumNumberOfCalls(10)
            .failureRateThreshold(10.0f)
            .permittedNumberOfCallsInHalfOpenState(10)
            .maxWaitDurationInHalfOpenState(Duration.ofMillis(100))
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .recordExceptions(UncheckedIOException.class)
            .build()
    );

    circuit.transitionToOpenState();
    circuit.transitionToHalfOpenState();

    testCircuitOnSuccess(Result.SUCCESS, State.HALF_OPEN, circuit, 3);

    Threads.sleep(101);

    testCircuitOnSuccess(Result.CIRCUIT_OPEN, State.OPEN, circuit, 1);

  }

  /**
   * Must run minimumNumberOfCalls in half open in  maxWaitDurationInHalfOpenState
   * so the algorithm can decide whether the circuit can be closed.
   */
  @Test
  void whenFailureThresholdingPeriodIsMetMustCloseCircuit() {

    final var circuit = CircuitBreaker.of(
        "defaultCircuitBreaker",
        CircuitBreakerConfig
            .custom()
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .minimumNumberOfCalls(10)
            .failureRateThreshold(10.0f)
            .permittedNumberOfCallsInHalfOpenState(10)
            .maxWaitDurationInHalfOpenState(Duration.ofMillis(100))
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .recordExceptions(UncheckedIOException.class)
            .build()
    );

    circuit.transitionToOpenState();
    circuit.transitionToHalfOpenState();

    testCircuitOnSuccess(Result.SUCCESS, State.HALF_OPEN, circuit, 9);

    Threads.sleep(50);

    testCircuitOnSuccess(Result.SUCCESS, State.CLOSED, circuit, 1);

  }

  private Object doSomething() {
    return null;
  }
}
