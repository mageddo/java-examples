package com.mageddo.coffeemaker.checkout;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.extension.incubator.metrics.ExtendedDoubleHistogramBuilder;
import io.opentelemetry.instrumentation.annotations.WithSpan;

@Component
public class CoffeeCheckoutMetrics {

  private final OpenTelemetry openTelemetry;

  private final LongCounter timesRan;

  private final DoubleHistogram timeToPrepare;

  @Autowired
  public CoffeeCheckoutMetrics(OpenTelemetry openTelemetry) {

    this.openTelemetry = openTelemetry;

    this.timesRan = this.openTelemetry // GlobalOpenTelemetry
        .getMeter("CoffeeCheckoutJob")
        .counterBuilder("timesRan")
        .setDescription("some detailed description for this metric")
        .build();

    this.timeToPrepare =  ((ExtendedDoubleHistogramBuilder) this.openTelemetry
        .getMeter("CoffeeCheckoutJob")
        .histogramBuilder("timeToOrderCoffee"))
        .setExplicitBucketBoundariesAdvice(Arrays.asList(50.0, 100.0, 120.0))
        .setUnit("ms")
        .build();
  }

  @WithSpan
  public LongCounter getTimesRan() {
    return this.timesRan;
  }

  @WithSpan
  public DoubleHistogram getTimeToPrepare() {
    return this.timeToPrepare;
  }
}
