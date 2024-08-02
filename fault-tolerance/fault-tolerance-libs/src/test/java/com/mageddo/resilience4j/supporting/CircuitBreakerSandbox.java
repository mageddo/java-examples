package com.mageddo.resilience4j.supporting;

import java.io.UncheckedIOException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

public interface CircuitBreakerSandbox {

  void testCircuitOnError(
      final Result expectedResult, final State expectedState, final int times
  );

  void testCircuitOnSuccess(
      final Result expectedResult, final State expectedState, final int times
  );

  static Result calcStats(Stats stats, Runnable r) {
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

  String runError();

  String runSuccess();

}
