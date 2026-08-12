package com.example.trend;

import org.apache.commons.math3.stat.descriptive.rank.Median;

public final class TheilSenSlopeAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.0025;
  private static final double STRONG = 0.01;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var delta = input.delta();
    final var slopes = new double[delta.length * (delta.length - 1) / 2];
    var position = 0;

    for (int i = 0; i < delta.length; i++) {
      for (int j = i + 1; j < delta.length; j++) {
        slopes[position++] = (delta[j] - delta[i]) / (j - i);
      }
    }

    final var slope = new Median().evaluate(slopes);

    return new TrendResult(
        "Theil-Sen slope",
        slope,
        DirectionalStatusClassifier.classify(slope, WARN, STRONG),
        "robust median pairwise slope"
    );
  }
}
