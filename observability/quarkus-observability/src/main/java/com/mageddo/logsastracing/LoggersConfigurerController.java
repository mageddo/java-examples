package com.mageddo.logsastracing;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class LoggersConfigurerController {

  private final LoggersConfigurerService loggersConfigurerService;

  @Inject
  public LoggersConfigurerController(LoggersConfigurerService loggersConfigurerService) {
    this.loggersConfigurerService = loggersConfigurerService;
  }

  public void configure() {
    this.loggersConfigurerService.configure();
  }
}
