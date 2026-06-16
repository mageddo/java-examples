package com.mageddo.observability.micrometer;

import java.time.Duration;
import java.util.Arrays;

import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

public class DistributionStatisticFactory {

  public static DistributionStatisticConfig build() {
    return DistributionStatisticConfig
        .builder()
        .percentilesHistogram(false)
        .serviceLevelObjectives(nanos(sloBuckets()))
        .build();
  }

  static Duration[] sloBuckets() {
    return new Duration[]{
        Duration.ofMillis(2),
        Duration.ofMillis(4),
        Duration.ofMillis(6),
        Duration.ofMillis(8),
        Duration.ofMillis(10),
        Duration.ofMillis(50),
        Duration.ofMillis(100),
        Duration.ofMillis(200),
        Duration.ofMillis(400),
        Duration.ofMillis(800),
        Duration.ofSeconds(1),
        Duration.ofMillis(1400),
        Duration.ofSeconds(2),
        Duration.ofSeconds(5),
        Duration.ofSeconds(10),
        Duration.ofSeconds(15),
        Duration.ofSeconds(30),
        Duration.ofSeconds(60),
        Duration.ofSeconds(90),
        Duration.ofSeconds(120),
        Duration.ofSeconds(300)
    };
  }

  private static double[] nanos(Duration... durations) {
    return Arrays
        .stream(durations)
        .mapToDouble(Duration::toNanos)
        .toArray();
  }
}
