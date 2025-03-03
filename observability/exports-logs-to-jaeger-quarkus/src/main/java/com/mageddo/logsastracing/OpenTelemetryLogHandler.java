package com.mageddo.logsastracing;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

public class OpenTelemetryLogHandler extends Handler {

  private final OpenTelemetry openTelemetry;

  public OpenTelemetryLogHandler() {
    this.openTelemetry = GlobalOpenTelemetry.get();
  }

  @Override
  public void publish(LogRecord record) {
    if (isLoggable(record)) {
      final var spanBuilder = this.openTelemetry.getTracer(record.getSourceClassName())
          .spanBuilder(record.getSourceClassName())
          .startSpan()
          .setAttribute("class", record.getSourceClassName())
          .setAttribute("method", record.getSourceMethodName())
          .setAttribute("level", record.getLevel().getName())
          .setAttribute("threadId", record.getLongThreadID())
          .setAttribute("log", record.getMessage());

      this.setStatus(spanBuilder, record);

      spanBuilder.end();


    }
  }

  private void setStatus(Span spanBuilder, LogRecord record) {
    if (record.getThrown() == null) {
      spanBuilder.setStatus(StatusCode.OK);
    } else {
      spanBuilder
          .recordException(record.getThrown())
          .setStatus(StatusCode.ERROR, record.getMessage())
      ;
    }

  }

  @Override
  public void flush() {
  }

  @Override
  public void close() {

  }
}
