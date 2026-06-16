package com.mageddo.observability.micrometer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

@Configuration
public class TimerHistogramConfigurer {

  private final DistributionStatisticConfig defaults;

  public TimerHistogramConfigurer() {
    this.defaults = DistributionStatisticFactory.build();
  }

  @Bean
  MeterFilter defaultTimerBuckets() {
    final var defaults = this.defaults;
    return new MeterFilter() {
      @Override
      public DistributionStatisticConfig configure(
          Meter.Id id, DistributionStatisticConfig config
      ) {

        if (id.getType() != Meter.Type.TIMER) {
          return config;
        }

        final var buckets = config.getServiceLevelObjectiveBoundaries();
        if(buckets != null && buckets.length > 0){
          return config;
        }

        return config.merge(defaults);
      }
    };
  }

}
