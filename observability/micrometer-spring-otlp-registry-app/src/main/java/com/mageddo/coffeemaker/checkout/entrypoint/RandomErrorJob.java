package com.mageddo.coffeemaker.checkout.entrypoint;

import io.micrometer.core.annotation.Timed;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RandomErrorJob {

  @Timed(
      value = "duration.ms",
      extraTags = {
          "span_name", "RandomErrorJob.run",
          "span_kind", "INTERNAL" // SERVER, CONSUMER, CLIENT
      }
  )
  @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
  public void run() {
    throw new UnsupportedOperationException();
  }
}
