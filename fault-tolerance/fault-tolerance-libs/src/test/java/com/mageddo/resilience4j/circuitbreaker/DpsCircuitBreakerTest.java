package com.mageddo.resilience4j.circuitbreaker;

import java.io.UncheckedIOException;
import java.time.Duration;

import com.mageddo.concurrency.Threads;
import com.mageddo.supporting.sandbox.Result;

import org.junit.jupiter.api.Test;

import static com.mageddo.supporting.sandbox.resilience4j.Resilience4jCircuitBreakerSandBox.testCircuitOnError;
import static com.mageddo.supporting.sandbox.resilience4j.Resilience4jCircuitBreakerSandBox.testCircuitOnSuccess;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DpsCircuitBreakerTest {

  @Test
  void serverGoesDown() {

    // arrange
    final var circuit = dpsConfig();

    // act // assert
    testCircuitOnError(Result.ERROR, CircuitBreaker.State.CLOSED, circuit, 99);
    testCircuitOnError(Result.ERROR, CircuitBreaker.State.OPEN, circuit, 1);

  }

  @Test
  void serverGotUp() {

    // arrange
    final var circuit = dpsConfig();
    circuit.transitionToOpenState();
    circuit.transitionToHalfOpenState();

    // act // assert
    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.HALF_OPEN, circuit, 1);
    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.CLOSED, circuit, 1);

  }


  @Test
  void serverGoesDownAndDecideToOpenTheCircuitAfterMinimumNumberOfCalls() {

    // arrange
    final var circuit = dpsConfig();

    // act // assert
    testCircuitOnError(Result.ERROR, CircuitBreaker.State.CLOSED, circuit, 4);
    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.CLOSED, circuit, 1);
    testCircuitOnError(Result.ERROR, CircuitBreaker.State.CLOSED, circuit, 94);
    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.OPEN, circuit, 1);

  }

  @Test
  void mustHalfOpenAutomatically() {

    final var circuit = dpsConfig();
    circuit.transitionToOpenState();

    Threads.sleep(1100);

    assertEquals(CircuitBreaker.State.HALF_OPEN, circuit.getState());
    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.HALF_OPEN, circuit, 1);

  }

  @Test
  void mustHalfOpenTestAndCloseTheCircuit() {

    // arrange
    final var circuit = dpsConfig();
    circuit.transitionToOpenState();
    Threads.sleep(1100);
    assertEquals(CircuitBreaker.State.HALF_OPEN, circuit.getState());

    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.HALF_OPEN, circuit, 1);
    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.CLOSED, circuit, 1);

  }

  @Test
  void mustOpenTheCircuitAfterHalfOpenAndTest() {

    // arrange
    final var circuit = dpsConfig();
    circuit.transitionToOpenState();
    circuit.transitionToHalfOpenState();
    Threads.sleep(1100);
    assertEquals(CircuitBreaker.State.HALF_OPEN, circuit.getState());

    testCircuitOnSuccess(Result.SUCCESS, CircuitBreaker.State.HALF_OPEN, circuit, 1);
    testCircuitOnError(Result.ERROR, CircuitBreaker.State.OPEN, circuit, 1);

  }

  static CircuitBreaker dpsConfig() {
    final var circuit = CircuitBreaker.of(
        "defaultCircuitBreaker",
        CircuitBreakerConfig
            .custom()
            .failureRateThreshold(21f)
            .minimumNumberOfCalls(100)
            .enableAutomaticTransitionFromOpenToHalfOpen()
            .permittedNumberOfCallsInHalfOpenState(2)
            .waitDurationInOpenState(Duration.ofSeconds(1))
            .recordExceptions(UncheckedIOException.class)
            .transitionOnResult(it -> {
              if(!it.isEmpty()) { // error
                log.info(
                    "empty={}, ex={}, it={}",
                    it.isEmpty(), it.getOrNull(), it
                );
              } else{
                log.info("left={}", it.getLeft());
              }
//              return CircuitBreakerConfig.TransitionCheckResult.transitionToOpen();
              return keepTheNormalTransition();
            })
            .build()
    );
    return circuit;
  }

  static CircuitBreakerConfig.TransitionCheckResult keepTheNormalTransition() {
    return CircuitBreakerConfig.TransitionCheckResult.noTransition();
  }
}
