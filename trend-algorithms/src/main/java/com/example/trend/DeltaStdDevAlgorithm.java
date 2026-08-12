package com.example.trend;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public final class DeltaStdDevAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.02;
  private static final double STRONG = 0.05;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var value = new StandardDeviation().evaluate(input.delta());
    final var status = value >= STRONG
        ? TrendStatus.VERY_HIGH_VARIATION
        : value >= WARN
            ? TrendStatus.HIGH_VARIATION
            : TrendStatus.STABLE;

    return new TrendResult(
        "Delta stddev",
        value,
        status,
        "volatility of paired differences"
    );
  }
}
