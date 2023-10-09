package com.mageddo.coffeemaker.checkout;

import java.util.Arrays;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.extension.incubator.metrics.ExtendedDoubleHistogramBuilder;

public class JobMetric {

  public static final LongCounter TIMES_RAN = GlobalOpenTelemetry.getMeter("CoffeeCheckoutJob")
      .counterBuilder("timesRan")
      .setDescription("some detailed description for this metric")
      .build();

  public static final DoubleHistogram TIME_TO_PREPARE =
      ((ExtendedDoubleHistogramBuilder) GlobalOpenTelemetry.getMeter("CoffeeCheckoutJob")
          .histogramBuilder("orderedSize"))
          .setExplicitBucketBoundariesAdvice(Arrays.asList(50.0, 100.0, 120.0))
          .setUnit("ms")
          .build();
}
