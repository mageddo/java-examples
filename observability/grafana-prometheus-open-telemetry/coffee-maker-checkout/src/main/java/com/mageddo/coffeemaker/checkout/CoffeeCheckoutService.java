package com.mageddo.coffeemaker.checkout;

import org.springframework.stereotype.Service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoffeeCheckoutService {

  @WithSpan
  public void checkout(CoffeeCheckoutReq req){
    log.info("status=done, req={}", req);
  }
}
