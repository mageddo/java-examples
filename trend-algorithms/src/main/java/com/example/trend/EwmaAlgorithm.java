package com.example.trend;

public final class EwmaAlgorithm implements TrendAlgorithm {

  private static final double EXPECTED_SIGMA = 0.02;
  private static final double LAMBDA = 0.30;
  private static final double CONTROL_LIMIT = 3.0;

  @Override
  public TrendResult analyze(TrendInput input) {
    var value = 0.0;

    for (final var sample : input.delta()) {
      value = LAMBDA * sample + (1.0 - LAMBDA) * value;
    }

    final var sigma = EXPECTED_SIGMA * Math.sqrt(
        LAMBDA / (2.0 - LAMBDA)
            * (1.0 - Math.pow(1.0 - LAMBDA, 2.0 * input.delta().length))
    );
    final var limit = CONTROL_LIMIT * sigma;
    final var status = value < -limit
        ? TrendStatus.ANOMALOUS_DEGRADATION
        : value > limit
            ? TrendStatus.ANOMALOUS_IMPROVEMENT
            : TrendStatus.NORMAL;

    return new TrendResult(
        "EWMA",
        value,
        status,
        "controlLimit=±%.6f lambda=%.2f sigma=%.4f".formatted(
            limit,
            LAMBDA,
            EXPECTED_SIGMA
        )
    );
  }
}
