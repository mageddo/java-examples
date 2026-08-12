package com.example.trend;

public class TrendAlgorithmsSmokeTest {

  public static void main(String[] args) {
    final var reference = new double[]{0.90, 0.90, 0.90, 0.90, 0.90, 0.90};
    final var current = new double[]{0.90, 0.89, 0.87, 0.84, 0.80, 0.75};
    final var input = TrendInput.of(current, reference);

    assertClose(-0.05833333333333333, new MeanDeltaAlgorithm().analyze(input).value(), 1e-12);
    assertTrue(new OlsSlopeAlgorithm().analyze(input).value() < 0);
    assertTrue(new CusumAlgorithm().analyze(input).status() == TrendStatus.ANOMALOUS_DEGRADATION);
  }

  private static void assertClose(double expected, double actual, double tolerance) {
    if (Math.abs(expected - actual) > tolerance) {
      throw new AssertionError("expected=" + expected + ", actual=" + actual);
    }
  }

  private static void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError("condition is false");
    }
  }
}
