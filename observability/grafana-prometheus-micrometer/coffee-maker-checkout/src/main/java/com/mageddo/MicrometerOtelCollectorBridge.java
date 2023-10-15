package com.mageddo;

/**
 * It wasn't necessary o.O
 * https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/096d32106a1baaf7d09290142e04ee1a9036ae64/instrumentation/micrometer/micrometer-1.5/library/src/main/java/io/opentelemetry/instrumentation/micrometer/v1_5/OpenTelemetryMeterRegistry.java
 * https://grafana.com/blog/2022/05/04/how-to-capture-spring-boot-metrics-with-the-opentelemetry-java-instrumentation-agent/
 */
//@Configuration
public class MicrometerOtelCollectorBridge {

//  @Bean
//  @ConditionalOnClass(name = "io.opentelemetry.javaagent.OpenTelemetryAgent")
//  public MeterRegistry otelRegistry() {
//    final var otelRegistry = Metrics.globalRegistry.getRegistries().stream()
//        .filter(r -> r.getClass().getName().contains("OpenTelemetryMeterRegistry"))
//        .findAny();
//    otelRegistry.ifPresent(Metrics.globalRegistry::remove);
//    return otelRegistry.orElse(null);
//  }
}
