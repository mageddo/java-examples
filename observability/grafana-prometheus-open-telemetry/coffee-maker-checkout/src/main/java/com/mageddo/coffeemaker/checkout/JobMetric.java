package com.mageddo.coffeemaker.checkout;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;

public class JobMetric {
  public static final LongCounter INSTANCE = GlobalOpenTelemetry.getMeter("CoffeeCheckoutJob")
      .counterBuilder("timesRan")
      .setDescription("some detailed description for this metric")
      .build();
}
