package com.mageddo.resilience4j.circuitbreaker;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

import com.mageddo.concurrency.Threads;
import com.mageddo.failsafe.Result;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

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
  void whenFailureThresholdingPeriodIsMetMustCloseCircuit() {
    final var circuit = CircuitBreaker.of(
        "defaultCircuitBreaker",
        CircuitBreakerConfig
            .custom()
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .failureRateThreshold(10.0f)
            .permittedNumberOfCallsInHalfOpenState(10)
            .maxWaitDurationInHalfOpenState(Duration.ofMillis(100))
            .waitDurationInOpenState(Duration.ofMillis(80))
            .recordExceptions(UncheckedIOException.class)
            .build()
    );

    circuit.transitionToHalfOpenState();


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

    final var decoratedSupplier = CircuitBreaker.decorateSupplier(circuit, this::doSomething);
    Object result = circuit.executeSupplier(decoratedSupplier);

    System.out.println(result);
  }

  private Object doSomething() {
    return null;
  }
}
