package com.mageddo.coffeemaker.checkout;

import java.util.Random;

import com.mageddo.commons.Threads;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.tracing.annotation.NewSpan;
import io.opentelemetry.api.GlobalOpenTelemetry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoffeeCheckoutService {

  private final Random r = new Random();
  private final CoffeeCheckoutMetrics metrics;

  @Autowired
  public CoffeeCheckoutService(CoffeeCheckoutMetrics metrics) {
    this.metrics = metrics;
  }

  @NewSpan
  public void checkout(CoffeeCheckoutReq req) {

    final var stopWatch = StopWatch.createStarted();

//    Threads.sleep(this.r.nextInt(10, 105));
    if(this.r.nextBoolean()){
      Threads.sleep(10);
    } else {
      Threads.sleep(105);
    }
    final var time = stopWatch.getTime();

    this.metrics.getTimesRan().increment(1);
    this.metrics.getTimeToPrepare().record(time);

    log.info(
        "status=done, time={}, req={}, otel={}, provider={}",
        time, req, GlobalOpenTelemetry.get(), GlobalOpenTelemetry.get().getMeterProvider()
    );
  }
}
