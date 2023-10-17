package com.mageddo.coffeemaker.checkout;

import org.springframework.stereotype.Component;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoffeeRepository {
  @WithSpan
  public void save(CoffeeCheckoutReq o){
    log.info("status=saved, o={}", o);
  }
}
