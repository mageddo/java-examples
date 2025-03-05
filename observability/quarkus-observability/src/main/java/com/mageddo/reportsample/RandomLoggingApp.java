package com.mageddo.reportsample;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.scheduler.Scheduled;

public class RandomLoggingApp {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  /***
   * Every log should be reported as tracing to Jaeger
   */
  @Scheduled(every = "2s")
  public void randomLog() throws Exception {
    log.info("status=ran, randomUuid={}", UUID.randomUUID());
  }


}
