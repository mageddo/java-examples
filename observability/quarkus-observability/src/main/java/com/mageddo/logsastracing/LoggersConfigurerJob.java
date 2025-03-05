package com.mageddo.logsastracing;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

@Slf4j
@Singleton
public class LoggersConfigurerJob {

  private final LoggersConfigurerService loggersConfigurerService;

  @Inject
  public LoggersConfigurerJob(LoggersConfigurerService loggersConfigurerService) {
    this.loggersConfigurerService = loggersConfigurerService;
  }

  @Scheduled(every = "10s", concurrentExecution = SKIP)
  public void configure() {
    log.debug("status=configuring");
    this.loggersConfigurerService.configure();
  }
}
