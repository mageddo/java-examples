package com.mageddo.coffeemaker.checkout;

import java.util.Random;

import com.mageddo.commons.Threads;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoffeeCheckoutService {

  private final Random r = new Random();

  @WithSpan
  public void checkout(CoffeeCheckoutReq req) {

    final var stopWatch = StopWatch.createStarted();

    Threads.sleep(this.r.nextInt(10, 80));
    final var time = stopWatch.getTime();

    JobMetric.TIMES_RAN.add(1);
    JobMetric.TIME_TO_PREPARE.record(time);

    log.info(
        "status=done, time={}, req={}, otel={}, provider={}",
        time, req, GlobalOpenTelemetry.get(), GlobalOpenTelemetry.get().getMeterProvider()
    );
  }
}
