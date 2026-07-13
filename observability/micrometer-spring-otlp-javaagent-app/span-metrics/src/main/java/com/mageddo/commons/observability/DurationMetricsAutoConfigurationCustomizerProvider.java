package com.mageddo.commons.observability;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;

/**
 * SPI carregada pelo OpenTelemetry Java Agent (via {@code -Dotel.javaagent.extensions}) que
 * anexa o {@link OtelDurationMetricsSpanProcessor} ao {@code SdkTracerProvider} do agent,
 * garantindo que ele seja invocado a cada span finalizado.
 */
public class DurationMetricsAutoConfigurationCustomizerProvider
    implements AutoConfigurationCustomizerProvider {

  @Override
  public void customize(AutoConfigurationCustomizer autoConfiguration) {
    autoConfiguration.addTracerProviderCustomizer(
        (tracerProvider, config) ->
            tracerProvider.addSpanProcessor(new OtelDurationMetricsSpanProcessor())
    );
  }
}
