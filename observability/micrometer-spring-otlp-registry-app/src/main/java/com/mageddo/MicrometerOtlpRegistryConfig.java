package com.mageddo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Clock;
import io.micrometer.registry.otlp.OtlpConfig;
import io.micrometer.registry.otlp.OtlpMeterRegistry;

@Configuration
public class MicrometerOtlpRegistryConfig {

  @Bean
  public OtlpMeterRegistry otelRegistry() {
    final var registry = new OtlpMeterRegistry(new OtlpConfig() {
      @Override
      public String get(String key) {
        return System.getProperty(key);
      }
    }, Clock.SYSTEM);
    registry
        .config()
        .commonTags("app_name", "default_app")
    ;
    return registry;
  }

}
