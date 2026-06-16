package com.mageddo.coffeemaker.checkout.entrypoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.mageddo.coffeemaker.checkout.CoffeeCheckoutReq;
import com.mageddo.coffeemaker.checkout.CoffeeCheckoutService;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoffeeCheckoutJob {

  private final CoffeeCheckoutService coffeeCheckoutService;

  @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
  public void checkout(){

    final var stopWatch = StopWatch.createStarted();

    final var coffee = makeACoffeeRequest();
    log.info("status=orderingACoffee, coffee={}", coffee);
    this.coffeeCheckoutService.checkout(coffee);

    DistributionSummary
        .builder("duration.ms")
        .tag("span_name", "CoffeeCheckoutJob.checkout")
        .tag("span_kind", "INTERNAL")
//            .serviceLevelObjectives(1, 10, 5) // se não quiser usar o padrão da app
        .register(Metrics.globalRegistry)
        .record(stopWatch.getTime());
  }

  static CoffeeCheckoutReq makeACoffeeRequest() {
    return new CoffeeCheckoutReq()
        .setCoffeeName("Preto")
        .setAmount(BigDecimal.valueOf(1 + Math.random() * 10).setScale(2, RoundingMode.HALF_UP));
  }
}
