package com.example.trend;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public final class PearsonTrendAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.40;
  private static final double STRONG = 0.70;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var value = new PearsonsCorrelation().correlation(input.time(), input.delta());

    return new TrendResult(
        "Pearson(time, delta)",
        value,
        DirectionalStatusClassifier.classify(value, WARN, STRONG),
        "linear direction strength"
    );
  }
}
