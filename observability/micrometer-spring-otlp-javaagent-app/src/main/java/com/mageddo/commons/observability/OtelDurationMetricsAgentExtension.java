//package com.mageddo.commons.observability;
//
//import com.google.auto.service.AutoService;
//
//import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
//import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
//
//@AutoService(AutoConfigurationCustomizerProvider.class)
//public final class OtelDurationMetricsAgentExtension
//    implements AutoConfigurationCustomizerProvider {
//
//  @Override
//  public void customize(AutoConfigurationCustomizer customizer) {
//    customizer.addTracerProviderCustomizer(
//        (builder, properties) -> builder.addSpanProcessor(
//            new DebugSpanProcessor()
//        )
//    );
//  }
//}
