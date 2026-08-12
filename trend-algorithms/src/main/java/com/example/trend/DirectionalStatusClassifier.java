package com.example.trend;

final class DirectionalStatusClassifier {

  private DirectionalStatusClassifier() {
  }

  static TrendStatus classify(double value, double warn, double strong) {
    if (!Double.isFinite(value)) {
      return TrendStatus.UNDEFINED;
    }

    if (value <= -strong) {
      return TrendStatus.STRONG_DEGRADATION;
    }

    if (value <= -warn) {
      return TrendStatus.DEGRADATION;
    }

    if (value >= strong) {
      return TrendStatus.STRONG_IMPROVEMENT;
    }

    if (value >= warn) {
      return TrendStatus.IMPROVEMENT;
    }

    return TrendStatus.NORMAL;
  }
}
