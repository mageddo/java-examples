package com.mageddo.coffeemaker.checkout;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.Getter;

@Component
public class CoffeeCheckoutMetrics {

  @Getter
  private final Counter timesRan;

  @Getter(onMethod = @__({@WithSpan}))
  private final DistributionSummary timeToPrepare;

  public CoffeeCheckoutMetrics() {

    /**
     * Metric example at Prometheus:
     * otel_timesRan{
     *   app_name="micrometer-spring-otlp-registry-app",
     *   exported_job="com.mageddo/unknown_service",
     *   instance="otel-collector.docker:9595",
     *   job="otel", service_id="123", service_name="unknown_service",
     *   service_namespace="com.mageddo", service_version="2.1.9",
     *   telemetry_sdk_language="java", telemetry_sdk_name="io.micrometer",
     *   telemetry_sdk_version="1.11.5",
     *   type="counter"
     * }
     */
    this.timesRan = Counter
        .builder("timesRan")
        .description("some detailed description for this metric")
        .tag("type", "counter")
        .register(Metrics.globalRegistry)
    ;

    this.timeToPrepare = DistributionSummary
        .builder("timeToOrderCoffee")
        .serviceLevelObjectives(50.0, 100.0, 120.0)
        .baseUnit("ms")
        .tag("priority", "P3")
        .register(Metrics.globalRegistry);
  }

}
