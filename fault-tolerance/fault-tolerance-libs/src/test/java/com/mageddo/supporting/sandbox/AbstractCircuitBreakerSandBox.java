package com.mageddo.supporting.sandbox;

import org.apache.commons.lang3.time.StopWatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractCircuitBreakerSandBox implements CircuitBreakerSandbox {

  protected abstract State getCircuitBreakerState();

  @Override
  public void testCircuitOnError(
      final Result expectedResult, final State expectedState, final int times
  ) {
    testCircuitOn(
        expectedResult, expectedState, times, this::runError
    );
  }

  @Override
  public void testCircuitOnSuccess(
      final Result expectedResult, final State expectedState, final int times
  ) {
    testCircuitOn(expectedResult, expectedState, times, this::runSuccess);
  }

  private void testCircuitOn(
      final Result expectedResult, final State expectedState,
      final int times, final Runnable runnable
  ) {
    final var stats = new Stats();
    final var stopWatch = StopWatch.createStarted();
    for (int i = 0; i < times; i++) {
      assertEquals(expectedResult, this.calcStats(stats, runnable));
      assertCircuitState(i, stopWatch, expectedState, this.getCircuitBreakerState());
    }
  }

  private static void assertCircuitState(
      int i, StopWatch stopWatch,
      final State expectedState,
      final State actualState
  ) {
    assertEquals(
        expectedState,
        actualState,
        formatMessage(i, stopWatch)
    );
  }

  private static String formatMessage(int i, StopWatch stopWatch) {
    return String.format("try=%d, time=%d", i, stopWatch.getTime());
  }

}
