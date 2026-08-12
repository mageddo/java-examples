package com.example.trend;

import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

public final class WilcoxonAlgorithm implements TrendAlgorithm {

  private static final double ALPHA = 0.05;

  @Override
  public TrendResult analyze(TrendInput input) {
    final var medianDelta = new Median().evaluate(input.delta());
    var hasDifference = false;

    for (final var value : input.delta()) {
      if (value != 0.0) {
        hasDifference = true;
        break;
      }
    }

    if (!hasDifference) {
      return new TrendResult(
          "Wilcoxon",
          1.0,
          TrendStatus.NO_SIGNIFICANT_CHANGE,
          "medianDelta=0.000000"
      );
    }

    final var exact = input.current().length <= 30;
    final var pValue = new WilcoxonSignedRankTest().wilcoxonSignedRankTest(
        input.reference(),
        input.current(),
        exact
    );
    final var status = pValue >= ALPHA
        ? TrendStatus.NO_SIGNIFICANT_CHANGE
        : medianDelta < 0
            ? TrendStatus.SIGNIFICANT_DEGRADATION
            : TrendStatus.SIGNIFICANT_IMPROVEMENT;

    return new TrendResult(
        "Wilcoxon",
        pValue,
        status,
        "medianDelta=%.6f exact=%s".formatted(medianDelta, exact)
    );
  }
}
