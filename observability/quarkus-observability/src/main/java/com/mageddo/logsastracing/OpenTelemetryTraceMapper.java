package com.mageddo.logsastracing;

import java.util.logging.LogRecord;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

public class OpenTelemetryTraceMapper {

  public static void of(OpenTelemetry openTelemetry, LogRecord record) {

    final var spanBuilder = openTelemetry.getTracer(record.getSourceClassName())
        .spanBuilder(record.getSourceClassName())
        .startSpan()
        .setAttribute("class", record.getSourceClassName())
        .setAttribute("method", record.getSourceMethodName())
        .setAttribute("level", record.getLevel().getName())
        .setAttribute("threadId", record.getLongThreadID())
        .setAttribute("log", record.getMessage());

    setStatus(spanBuilder, record);

    spanBuilder.end();

  }

  private static void setStatus(Span spanBuilder, LogRecord record) {
    if (record.getThrown() == null) {
      spanBuilder.setStatus(StatusCode.OK);
    } else {
      spanBuilder
          .recordException(record.getThrown())
          .setStatus(StatusCode.ERROR, record.getMessage())
      ;
    }

  }
}
