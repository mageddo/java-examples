package com.mageddo.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import org.jboss.logmanager.LogContext;
import org.jboss.logmanager.Logger.AttachmentKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OpenTelemetryLogReportAsTraceConfigurer {

  private static final Logger log =
      LoggerFactory.getLogger(OpenTelemetryLogReportAsTraceConfigurer.class);

  public void configure() {
    final var openTelemetry = GlobalOpenTelemetry.get();
    final var loggers = this.findSlf4LoggersFromJbossLogManager();
//    installOpenTelemetryLogAppender(openTelemetry, null);
  }

  /**
   * @see org.slf4j.impl.Slf4jLoggerFactory
   */

  private List<Logger> findSlf4LoggersFromJbossLogManager() {
    final var loggerNames = LogContext.getLogContext().getLoggerNames();
    final var loggers = new ArrayList<Logger>();
    while (loggerNames.hasMoreElements()) {

      final var logger = findSlf4LoggerFrom(loggerNames.nextElement());
      loggers.add(logger);

    }
    return loggers;
  }

  private static final AttachmentKey<Logger> key = new AttachmentKey<>();

  private static Logger findSlf4LoggerFrom(final String loggerName) {
    final var jbossLogger = LogContext.getLogContext().getLogger(loggerName);
    return jbossLogger.getAttachment(key);
  }

//
//  private void installOpenTelemetryLogAppender(
//      final OpenTelemetry openTelemetry,
//      final List<ch.qos.logback.classic.Logger> loggers
//  ) {
//    for (ch.qos.logback.classic.Logger logger : loggers) {
//      logger
//          .iteratorForAppenders()
//          .forEachRemaining((appender) -> {
//            if (appender instanceof OpenTelemetryAppender) {
//              ((OpenTelemetryAppender) appender).setOpenTelemetry(openTelemetry);
//            }
//          });
//    }
//  }
}
