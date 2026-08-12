package com.example.trend;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

public final class KendallTrendAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.40;
  private static final double STRONG = 0.70;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var value = new KendallsCorrelation().correlation(input.time(), input.delta());

    return new TrendResult(
        "Kendall(time, delta)",
        value,
        DirectionalStatusClassifier.classify(value, WARN, STRONG),
        "rank-based direction strength"
    );
  }
}
