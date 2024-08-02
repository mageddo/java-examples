package com.mageddo.resilience4j.supporting;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;

import com.mageddo.failsafe.Result;

import org.apache.commons.lang3.time.StopWatch;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Reslience4jTestUtils {

  public static void testCircuitOnError(
      final Result expectedResult, final CircuitBreaker.State expectedState,
      final CircuitBreaker circuit, final int times
  ) {
    testCircuitOn(expectedResult, expectedState, circuit, times, () -> runError(circuit));
  }

  public static void testCircuitOnSuccess(
      final Result expectedResult, final CircuitBreaker.State expectedState,
      final CircuitBreaker circuit, final int times
  ) {
    testCircuitOn(expectedResult, expectedState, circuit, times, () -> runSuccess(circuit));
  }

  public static void testCircuitOn(
      final Result expectedResult, final CircuitBreaker.State expectedState,
      final CircuitBreaker circuit, final int times, final Runnable runnable
  ) {
    final var stats = new Stats();
    final var stopWatch = StopWatch.createStarted();
    for (int i = 0; i < times; i++) {
      assertEquals(expectedResult, calcStats(stats, runnable));
      assertCircuitState(i, stopWatch, expectedState, circuit.getState());
    }
  }

  private static void assertCircuitState(int i, StopWatch stopWatch,
      final CircuitBreaker.State expectedState,
      final CircuitBreaker.State actualState) {
    assertEquals(
        expectedState,
        actualState,
        formatMessage(i, stopWatch)
    );
  }

  private static String formatMessage(int i, StopWatch stopWatch) {
    return String.format("try=%d, time=%d", i, stopWatch.getTime());
  }

  public static Result calcStats(Stats stats, Runnable r) {
    try {
      r.run();
      stats.success++;
      return Result.SUCCESS;
    } catch (CallNotPermittedException e) {
      stats.openCircuit++;
      return Result.CIRCUIT_OPEN;
    } catch (UncheckedIOException e) {
      stats.error++;
      return Result.ERROR;
    }
  }

  public static String runError(CircuitBreaker breaker) {
    return breaker.executeSupplier(() -> {
      throw new UncheckedIOException(new IOException("an error"));
    });
  }

  public static String runSuccess(CircuitBreaker breaker) {
    return breaker.executeSupplier(() -> LocalDateTime.now().toString());
  }

  public static class Stats {
    int success;
    int error;
    int openCircuit;
  }
}
