package com.mageddo.logsastracing;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;

public class OpenTelemetryLogHandler extends Handler {

  private final OpenTelemetry openTelemetry;

  public OpenTelemetryLogHandler() {
    this.openTelemetry = GlobalOpenTelemetry.get();
  }

  @Override
  public void publish(LogRecord record) {
    if (isLoggable(record)) {
      this.openTelemetry.getTracer(record.getSourceClassName())
          .spanBuilder(record.getSourceMethodName())
          .setAttribute("class", record.getSourceClassName())
          .setAttribute("level", record.getLevel().getName())
          .setAttribute("threadId", record.getLongThreadID())
          .setAttribute("log", record.getMessage())
          .startSpan()
          .end();
    }
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() {

  }
}
