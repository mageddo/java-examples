package com.mageddo.logsastracing;

import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class LoggersConfigurerService {

  private final LoggerDAO loggerDAO;

  @Inject
  public LoggersConfigurerService(LoggerDAO loggerDAO) {
    this.loggerDAO = loggerDAO;
  }

  public void configure() {
    final var configuredLoggers = this.loggerDAO.findConfiguredLoggersAsStringSet();
    final var foundLoggers = this.loggerDAO.findAvailableLoggers();
    foundLoggers.forEach(logger -> {
      final var key = logger.toString();
      if (configuredLoggers.contains(key)) {
        return;
      }
      this.configureLoggerToBeHandled(logger);
      log.debug("status=configured, name={}", logger.getName());
    });
  }

  private void configureLoggerToBeHandled(Logger logger) {
    logger.addHandler(new OpenTelemetryLogHandler());
    this.loggerDAO.addLogger(logger);
  }
}
