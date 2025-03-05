package com.mageddo.logsastracing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.logmanager.LogContext;

public class JbossLogManager {

  /**
   * @see org.slf4j.impl.Slf4jLoggerFactory
   */
  public static List<Logger> findAllLoggers() {
    final var loggerNames = LogContext.getLogContext().getLoggerNames();
    final var loggers = new ArrayList<Logger>();
    while (loggerNames.hasMoreElements()) {

      final var logger = findLogger(loggerNames.nextElement());
      loggers.add(logger);

    }
    return loggers;
  }

  private static Logger findLogger(final String loggerName) {
    return LogContext
        .getLogContext()
        .getLogger(loggerName);
  }
}
