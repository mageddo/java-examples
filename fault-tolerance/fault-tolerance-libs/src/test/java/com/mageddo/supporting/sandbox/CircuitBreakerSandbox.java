package com.mageddo.supporting.sandbox;

public interface CircuitBreakerSandbox {

  void testCircuitOnError(
      final Result expectedResult, final State expectedState, final int times
  );

  void testCircuitOnSuccess(
      final Result expectedResult, final State expectedState, final int times
  );

  Result calcStats(Stats stats, Runnable r);

  String runError();

  String runSuccess();

}
