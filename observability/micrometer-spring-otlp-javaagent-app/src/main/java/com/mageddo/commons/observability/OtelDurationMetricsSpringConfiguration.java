package com.mageddo.commons.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.sdk.trace.SpanProcessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OtelDurationMetricsSpringConfiguration {

  @Bean
  public SpanProcessor otelDurationMetricsSpanProcessor(MeterRegistry meterRegistry) {
    return new OtelDurationMetricsSpanProcessor(meterRegistry);
  }
}
