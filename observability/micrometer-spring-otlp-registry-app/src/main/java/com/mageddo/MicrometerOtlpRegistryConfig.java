package com.mageddo;

import org.springframework.beans.factory.annotation.Value;

import io.micrometer.core.instrument.Clock;
import io.micrometer.registry.otlp.OtlpConfig;
import io.micrometer.registry.otlp.OtlpMeterRegistry;

/**
 * Manual configuration alternative to spring `management.otlp.metrics` auto config
 * OtlpMetricsExportAutoConfiguration
 *
 */
//@Configuration
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
//  @Bean
  public OtlpMeterRegistry otelRegistry(@Value("${info.version}") String version) {
    final var registry = new OtlpMeterRegistry(new OtlpConfig() {
      @Override
      public String get(String key) {
        if (key.equals("otlp.resourceAttributes")) {
          return "telemetry.sdk.version=" + version;
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
