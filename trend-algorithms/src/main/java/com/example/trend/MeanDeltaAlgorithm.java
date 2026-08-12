package com.example.trend;

import org.apache.commons.math3.stat.StatUtils;

public final class MeanDeltaAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.02;
  private static final double STRONG = 0.05;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var value = StatUtils.mean(input.delta());

    return new TrendResult(
        "Mean delta",
        value,
        DirectionalStatusClassifier.classify(value, WARN, STRONG),
        "average paired difference"
    );
  }
}
