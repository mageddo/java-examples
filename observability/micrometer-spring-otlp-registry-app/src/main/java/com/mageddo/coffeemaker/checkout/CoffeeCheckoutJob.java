package com.mageddo.coffeemaker.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoffeeCheckoutJob {

  private final CoffeeCheckoutService coffeeCheckoutService;

  @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
  public void checkout(){
    final var coffee = makeACoffeeRequest();
    log.info("status=orderingACoffee, coffee={}", coffee);
    this.coffeeCheckoutService.checkout(coffee);
  }

  private static CoffeeCheckoutReq makeACoffeeRequest() {
    return new CoffeeCheckoutReq()
        .setCoffeeName("Preto")
        .setAmount(BigDecimal.valueOf(1 + Math.random() * 10).setScale(2, RoundingMode.HALF_UP));
  }
}
