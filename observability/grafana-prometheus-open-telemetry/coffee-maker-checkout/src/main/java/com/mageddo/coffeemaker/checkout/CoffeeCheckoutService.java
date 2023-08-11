package com.mageddo.coffeemaker.checkout;

import org.springframework.stereotype.Service;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoffeeCheckoutService {

  @WithSpan
  public void checkout(CoffeeCheckoutReq req){
    JobMetric.INSTANCE.add(1);

    log.info(
        "status=done, req={}, otel={}, provider={}", req, GlobalOpenTelemetry.get(),
        GlobalOpenTelemetry.get().getMeterProvider()
        );
  }
}
