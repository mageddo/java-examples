package com.mageddo.logsastracing;

import io.quarkus.scheduler.Scheduled;
import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

  private final LoggersConfigurerController loggersConfigurerController;

  @Inject
  public App(LoggersConfigurerController loggersConfigurerController) {
    this.loggersConfigurerController = loggersConfigurerController;
  }

  @Scheduled(every = "10s", concurrentExecution = SKIP)
  public void configure() {
    log.debug("status=configuring");
    this.loggersConfigurerController.configure();
  }

}
