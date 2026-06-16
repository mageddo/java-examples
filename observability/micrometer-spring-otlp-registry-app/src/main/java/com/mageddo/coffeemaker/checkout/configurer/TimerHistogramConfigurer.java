package com.mageddo.coffeemaker.checkout.configurer;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
public class TimerHistogramConfigurer {

  @Bean
  MeterFilter defaultTimerBuckets() {
    return new MeterFilter() {
      @Override
      public DistributionStatisticConfig configure(
          Meter.Id id, DistributionStatisticConfig config
      ) {

        if (id.getType() != Meter.Type.TIMER) {
          return config;
        }
        return DistributionStatisticConfig
            .builder()
            .percentilesHistogram(false)
            .serviceLevelObjectives(nanos(sloBuckets()))
            .build()
            .merge(config);
      }
    };
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
