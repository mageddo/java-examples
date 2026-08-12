package com.example.trend;

import java.util.Arrays;

public record TrendInput(
    double[] current,
    double[] reference,
    double[] delta,
    double[] time
) {

  public TrendInput {
    current = Arrays.copyOf(current, current.length);
    reference = Arrays.copyOf(reference, reference.length);
    delta = Arrays.copyOf(delta, delta.length);
    time = Arrays.copyOf(time, time.length);
  }

  public static TrendInput of(double[] current, double[] reference) {
    validate(current, reference);

    final var delta = new double[current.length];
    final var time = new double[current.length];

    for (int i = 0; i < current.length; i++) {
      delta[i] = current[i] - reference[i];
      time[i] = i;
    }

    return new TrendInput(current, reference, delta, time);
  }

  private static void validate(double[] current, double[] reference) {
    if (current == null || reference == null) {
      throw new IllegalArgumentException("Arrays cannot be null");
    }

    if (current.length != reference.length) {
      throw new IllegalArgumentException("Arrays must have the same size");
    }

    if (current.length < 3) {
      throw new IllegalArgumentException("At least 3 samples are required");
    }

    validateRates(current);
    validateRates(reference);
  }

  private static void validateRates(double[] values) {
    for (final var value : values) {
      if (!Double.isFinite(value) || value < 0 || value > 1) {
        throw new IllegalArgumentException(
            "All values must be finite rates between 0 and 1"
        );
      }
    }
  }
}
