package com.example.trend;

public final class CusumAlgorithm implements TrendAlgorithm {

  private static final double EXPECTED_SIGMA = 0.02;
  private static final double K = 0.5;
  private static final double H = 5.0;

  @Override
  public TrendResult analyze(TrendInput input) {
    var positive = 0.0;
    var negative = 0.0;
    var maxPositive = 0.0;
    var maxNegative = 0.0;

    for (final var sample : input.delta()) {
      final var z = sample / EXPECTED_SIGMA;

      positive = Math.max(0, positive + z - K);
      negative = Math.max(0, negative - z - K);

      maxPositive = Math.max(maxPositive, positive);
      maxNegative = Math.max(maxNegative, negative);
    }

    final var score = maxNegative > maxPositive
        ? -maxNegative / H
        : maxPositive / H;

    final TrendStatus status;

    if (maxNegative >= H && maxPositive >= H) {
      status = TrendStatus.MIXED_ANOMALY;
    } else if (maxNegative >= H) {
      status = TrendStatus.ANOMALOUS_DEGRADATION;
    } else if (maxPositive >= H) {
      status = TrendStatus.ANOMALOUS_IMPROVEMENT;
    } else {
      status = TrendStatus.NORMAL;
    }

    return new TrendResult(
        "CUSUM",
        score,
        status,
        "negative=%.3f positive=%.3f threshold=%.3f k=%.2f sigma=%.4f".formatted(
            maxNegative,
            maxPositive,
            H,
            K,
            EXPECTED_SIGMA
        )
    );
  }
}
