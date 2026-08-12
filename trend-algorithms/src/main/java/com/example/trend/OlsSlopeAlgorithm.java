package com.example.trend;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public final class OlsSlopeAlgorithm implements TrendAlgorithm {

  private static final double WARN = 0.0025;
  private static final double STRONG = 0.01;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var regression = new SimpleRegression(true);

    for (int i = 0; i < input.delta().length; i++) {
      regression.addData(i, input.delta()[i]);
    }

    final var slope = regression.getSlope();
    final var r2 = regression.getRSquare();
    final var pValue = regression.getSignificance();

    return new TrendResult(
        "OLS slope",
        slope,
        DirectionalStatusClassifier.classify(slope, WARN, STRONG),
        "r2=%.4f p=%.6f".formatted(r2, pValue)
    );
  }
}
