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

    this.timesRan = Counter
        .builder("timesRan")
        .description("some detailed description for this metric")
        .tag("type", "counter")
        .register(Metrics.globalRegistry)
    ;

    this.timeToPrepare =  DistributionSummary
        .builder("timeToOrderCoffee")
        .serviceLevelObjectives(50.0, 100.0, 120.0)
        .baseUnit("ms")
        .tag("type", "histogram")
        .register(Metrics.globalRegistry);
  }

}
