package com.mageddo;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.scheduler.Scheduled;

public class RandomLogging {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Scheduled(every = "2s")
  public void randomLog() throws Exception {
    log.info("status=ran, randomUuid={}", UUID.randomUUID().toString());
  }


}
