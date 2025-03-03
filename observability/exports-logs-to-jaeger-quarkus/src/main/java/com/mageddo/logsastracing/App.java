package com.mageddo.logsastracing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logmanager.Logger;

import io.quarkus.scheduler.Scheduled;
import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

  final Set<String> loggers = new HashSet<>();

  @Scheduled(every = "10s", concurrentExecution = SKIP)
  public void configure() {
    log.debug("status=configuring");
    final var foundLoggers = this.findMageddoLoggers();
    foundLoggers.forEach(logger -> {
      final var key = logger.toString();
      if (this.loggers.contains(key)) {
        return;
      }
      logger.addHandler(new OpenTelemetryLogHandler());
      this.loggers.add(key);
      log.debug("status=configured, name={}", logger.getName());
    });

  }

  private List<Logger> findMageddoLoggers() {
    final var loggers = JbossLogManager.findAllLoggers();
    return loggers;
  }
}
