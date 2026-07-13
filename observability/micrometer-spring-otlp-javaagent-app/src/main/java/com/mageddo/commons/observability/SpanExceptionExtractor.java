package com.mageddo.commons.observability;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.trace.data.SpanData;

public final class SpanExceptionExtractor {

  private static final AttributeKey<String> EXCEPTION_TYPE =
      AttributeKey.stringKey("exception.type");

  private static final AttributeKey<String> EXCEPTION_MESSAGE =
      AttributeKey.stringKey("exception.message");

  private static final AttributeKey<String> EXCEPTION_STACKTRACE =
      AttributeKey.stringKey("exception.stacktrace");

  private SpanExceptionExtractor() {
  }

  public static RecordedException extract(SpanData spanData) {
    return spanData
        .getEvents()
        .stream()
        .filter(event -> "exception".equals(event.getName()))
        .map(event -> {

          final var attributes = event.getAttributes();
          return new RecordedException(
              attributes.get(EXCEPTION_TYPE),
              attributes.get(EXCEPTION_MESSAGE),
              attributes.get(EXCEPTION_STACKTRACE)
          );

        })
        .findFirst()
        .orElse(null);
  }

  public record RecordedException(
      String type,
      String message,
      String stacktrace
  ) {
  }
}
