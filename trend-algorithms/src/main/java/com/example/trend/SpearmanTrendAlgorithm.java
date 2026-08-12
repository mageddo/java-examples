package com.example.trend;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public final class SpearmanTrendAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.40;
  private static final double STRONG = 0.70;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var value = new SpearmansCorrelation().correlation(input.time(), input.delta());

    return new TrendResult(
        "Spearman(time, delta)",
        value,
        DirectionalStatusClassifier.classify(value, WARN, STRONG),
        "monotonic direction strength"
    );
  }
}
