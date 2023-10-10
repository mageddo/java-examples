package com.mageddo.otel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class Otel {
  @Bean
  public OpenTelemetry openTelemetry() {
    log.info("configurando o otel");
    Resource resource = Resource.getDefault().toBuilder()
        .put(ResourceAttributes.SERVICE_NAME, "coffee-maker-checkout")
        .put(ResourceAttributes.SERVICE_VERSION, "0.1.0")
        .build();

    SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().build()).build())
        .setResource(resource)
        .build();

    SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
        .registerMetricReader(
            PeriodicMetricReader.builder(
                    OtlpGrpcMetricExporter
                        .builder()
                        .setEndpoint("http://otel-prometheus-collector.docker:4317")
//                        .setAggregationTemporalitySelector(AggregationTemporalitySelector.deltaPreferred())
                        .build()
                )
                .build()
        )
        .setResource(resource)
        .build();

    SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
        .addLogRecordProcessor(
            BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder().build()).build())
        .setResource(resource)
        .build();

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
        .setTracerProvider(sdkTracerProvider)
        .setMeterProvider(sdkMeterProvider)
        .setLoggerProvider(sdkLoggerProvider)
        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        .buildAndRegisterGlobal();

    return openTelemetry;
  }
}
