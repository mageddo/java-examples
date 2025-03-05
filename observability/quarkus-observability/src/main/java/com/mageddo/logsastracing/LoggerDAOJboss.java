package com.mageddo.logsastracing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.inject.Singleton;

@Singleton
public class LoggerDAOJboss implements LoggerDAO {

  private final Set<String> loggers = new HashSet<>();

  @Override
  public void addLogger(Logger logger) {
    this.loggers.add(logger.toString());
  }

  @Override
  public List<Logger> findAvailableLoggers() {
    return JbossLogManager.findAllLoggers();
  }

  @Override
  public Set<String> findConfiguredLoggersAsStringSet() {
    return this.loggers;
  }

}
