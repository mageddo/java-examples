package com.mageddo.commons.observability;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.trace.data.SpanData;

import lombok.Builder;
import lombok.Value;

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
          return RecordedException
              .builder()
              .type(attributes.get(EXCEPTION_TYPE))
              .message(attributes.get(EXCEPTION_MESSAGE))
              .stacktrace(attributes.get(EXCEPTION_STACKTRACE))
              .build();

        })
        .findFirst()
        .orElse(null);
  }

  @Value
  @Builder
  public static class RecordedException {
    String type;
    String message;
    String stacktrace;
  }
}
