package com.example.trend;

import org.apache.commons.math3.stat.descriptive.rank.Median;

public final class MedianDeltaAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.02;
  private static final double STRONG = 0.05;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var value = new Median().evaluate(input.delta());

    return new TrendResult(
        "Median delta",
        value,
        DirectionalStatusClassifier.classify(value, WARN, STRONG),
        "robust paired level difference"
    );
  }
}
