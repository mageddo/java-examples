package com.mageddo.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.registry.otlp.OtlpConfig;
import io.micrometer.registry.otlp.OtlpMeterRegistry;

/**
 * Manual configuration alternative to `quarkus-micrometer-opentelemetry` extension
 */
public class MicrometerOtlpRegistryConfig {

  /**
   * Properties I identified:
   * otlp.connectTimeout
   * otlp.readTimeout
   * otlp.batchSize
   * otlp.numThreads
   * otlp.url
   * otlp.resourceAttributes
   * otlp.aggregationTemporality
   * otlp.step
   * otlp.enabled
   * otlp.batchSize
   * otlp.url
   * otlp.headers
   */

//  @Produces
//  @Singleton
  public OtlpMeterRegistry otelRegistry() {
    final var registry = new OtlpMeterRegistry(new OtlpConfig() {
      @Override
      public String get(String key) {
        if (key.equals("otlp.resourceAttributes")) {
          return "telemetry.sdk.version=1.0.0";
        }
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
