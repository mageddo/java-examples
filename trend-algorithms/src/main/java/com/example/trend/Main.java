package com.example.trend;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    final var reference = new double[]{
        0.92, 0.91, 0.93, 0.92, 0.91, 0.92,
        0.93, 0.92, 0.91, 0.92, 0.93, 0.92
    };

    final var current = new double[]{
        0.92, 0.91, 0.91, 0.90, 0.89, 0.88,
        0.86, 0.84, 0.82, 0.80, 0.77, 0.74
    };

    final var input = TrendInput.of(current, reference);
    final var algorithms = List.<TrendAlgorithm>of(
        new MeanDeltaAlgorithm(),
        new MedianDeltaAlgorithm(),
        new DeltaStdDevAlgorithm(),
        new OlsSlopeAlgorithm(),
        new TheilSenSlopeAlgorithm(),
        new PearsonTrendAlgorithm(),
        new SpearmanTrendAlgorithm(),
        new KendallTrendAlgorithm(),
        new PairedTTestAlgorithm(),
        new WilcoxonAlgorithm(),
        new EwmaAlgorithm(),
        new CusumAlgorithm()
    );

    algorithms.stream()
        .map(algorithm -> algorithm.analyze(input))
        .forEach(Main::print);
  }

  private static void print(TrendResult result) {
    System.out.printf(
        "%-25s value=%10.6f  status=%-30s %s%n",
        result.metric(),
        result.value(),
        result.status(),
        result.details()
    );
  }
}
