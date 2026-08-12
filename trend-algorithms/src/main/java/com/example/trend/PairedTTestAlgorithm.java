package com.example.trend;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.inference.TTest;

public final class PairedTTestAlgorithm implements TrendAlgorithm {

  private static final double ALPHA = 0.05;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var meanDelta = StatUtils.mean(input.delta());

    if (meanDelta == 0.0) {
      return new TrendResult(
          "Paired t-test",
          1.0,
          TrendStatus.NO_SIGNIFICANT_CHANGE,
          "meanDelta=0.000000 alpha=%.2f".formatted(ALPHA)
      );
    }

    final var test = new TTest();
    final var pValue = test.pairedTTest(input.current(), input.reference());
    final var status = pValue >= ALPHA
        ? TrendStatus.NO_SIGNIFICANT_CHANGE
        : meanDelta < 0
            ? TrendStatus.SIGNIFICANT_DEGRADATION
            : TrendStatus.SIGNIFICANT_IMPROVEMENT;

    return new TrendResult(
        "Paired t-test",
        pValue,
        status,
        "meanDelta=%.6f alpha=%.2f".formatted(meanDelta, ALPHA)
    );
  }
}
