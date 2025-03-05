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
      OpenTelemetryTraceMapper.of(this.openTelemetry, record);
    }
  }

  @Override
  public void flush() {
  }

  @Override
  public void close() {

  }
}
