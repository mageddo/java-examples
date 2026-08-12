package com.example.trend;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class TrendAlgorithmsTest {

  @Test
  void shouldCalculatePairedDelta() {
    final var input = TrendInput.of(
        new double[]{0.90, 0.80, 0.70},
        new double[]{0.85, 0.80, 0.75}
    );

    assertArrayEquals(new double[]{0.05, 0.0, -0.05}, input.delta(), 1e-12);
    assertArrayEquals(new double[]{0.0, 1.0, 2.0}, input.time(), 1e-12);
  }

  @Test
  void shouldRejectInvalidInput() {
    assertThrows(
        IllegalArgumentException.class,
        () -> TrendInput.of(new double[]{0.1, 0.2}, new double[]{0.1, 0.2})
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> TrendInput.of(
            new double[]{0.1, 0.2, 1.1},
            new double[]{0.1, 0.2, 0.3}
        )
    );
  }

  @Test
  void shouldDetectClearDegradationAcrossAlgorithms() {
    final var input = degradationInput();

    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new MeanDeltaAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new MedianDeltaAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new OlsSlopeAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new TheilSenSlopeAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new PearsonTrendAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new SpearmanTrendAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.STRONG_DEGRADATION,
        new KendallTrendAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.SIGNIFICANT_DEGRADATION,
        new PairedTTestAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.SIGNIFICANT_DEGRADATION,
        new WilcoxonAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.ANOMALOUS_DEGRADATION,
        new EwmaAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.ANOMALOUS_DEGRADATION,
        new CusumAlgorithm().analyze(input).status()
    );
  }

  @Test
  void shouldStayNormalWhenSeriesAreEqual() {
    final var reference = new double[]{
        0.90, 0.90, 0.90, 0.90, 0.90, 0.90,
        0.90, 0.90, 0.90, 0.90, 0.90, 0.90
    };
    final var input = TrendInput.of(reference, reference);

    final var results = List.of(
        new MeanDeltaAlgorithm().analyze(input),
        new MedianDeltaAlgorithm().analyze(input),
        new OlsSlopeAlgorithm().analyze(input),
        new TheilSenSlopeAlgorithm().analyze(input),
        new EwmaAlgorithm().analyze(input),
        new CusumAlgorithm().analyze(input)
    );

    assertTrue(results.stream().allMatch(result -> result.status() == TrendStatus.NORMAL));
    assertEquals(TrendStatus.STABLE, new DeltaStdDevAlgorithm().analyze(input).status());
    assertEquals(
        TrendStatus.NO_SIGNIFICANT_CHANGE,
        new PairedTTestAlgorithm().analyze(input).status()
    );
    assertEquals(
        TrendStatus.NO_SIGNIFICANT_CHANGE,
        new WilcoxonAlgorithm().analyze(input).status()
    );
    assertEquals(TrendStatus.UNDEFINED, new PearsonTrendAlgorithm().analyze(input).status());
    assertEquals(TrendStatus.UNDEFINED, new SpearmanTrendAlgorithm().analyze(input).status());
    assertEquals(TrendStatus.UNDEFINED, new KendallTrendAlgorithm().analyze(input).status());
  }

  private static TrendInput degradationInput() {
    final var reference = new double[]{
        0.92, 0.91, 0.93, 0.92, 0.91, 0.92,
        0.93, 0.92, 0.91, 0.92, 0.93, 0.92
    };
    final var current = new double[]{
        0.92, 0.91, 0.91, 0.90, 0.89, 0.88,
        0.86, 0.84, 0.82, 0.80, 0.77, 0.74
    };

    return TrendInput.of(current, reference);
  }
}
