package com.mageddo.commons.observability;

import com.mageddo.commons.observability.SpanExceptionExtractor.RecordedException;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.SpanData;
import lombok.Builder;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class OtelDurationMetricsSpanProcessor implements SpanProcessor {

  public static final String METRIC_NAME = "custom_duration_ms";

  static final Duration[] BUCKETS = {
      Duration.ofMillis(1),
      Duration.ofMillis(2),
      Duration.ofMillis(4),
      Duration.ofMillis(5),
      Duration.ofMillis(8),
      Duration.ofMillis(10),
      Duration.ofMillis(30),
      Duration.ofMillis(50)
  };

  private final MeterRegistry meterRegistry;

  private final ConcurrentMap<MetricKey, Timer> timers = new ConcurrentHashMap<>();

  public OtelDurationMetricsSpanProcessor(MeterRegistry meterRegistry) {
    this.meterRegistry = Objects.requireNonNull(meterRegistry);
  }

  @Override
  public void onStart(Context parentContext, ReadWriteSpan span) {
    // Não precisamos interceptar o início.
  }

  @Override
  public boolean isStartRequired() {
    return false;
  }

  @Override
  public void onEnd(ReadableSpan span) {
    try {
      this.record(span);
    } catch (RuntimeException ignored) {
    }
  }

  void record(ReadableSpan span) {

    final var spanData = span.toSpanData();
    final var dur = Duration.ofNanos(
        spanData.getEndEpochNanos() - spanData.getStartEpochNanos()
    );

    if (dur.isZero()) {
      return;
    }

    final var key = MetricKey.from(spanData);
    final var timer = this.timers.computeIfAbsent(
        key,
        this::createTimer
    );

    timer.record(dur);
  }

  @Override
  public boolean isEndRequired() {
    return true;
  }

  @Override
  public CompletableResultCode forceFlush() {
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode shutdown() {
    this.timers.clear();
    return CompletableResultCode.ofSuccess();
  }

  Timer createTimer(MetricKey key) {
    return Timer
        .builder(METRIC_NAME)
        .description("Duration of completed OpenTelemetry spans")
        .tags(
            "span_name", key.spanName(),
            "status_code", key.statusCode(),
            "span_kind", key.spanKind()
        )
        .serviceLevelObjectives(BUCKETS)
        .register(this.meterRegistry);
  }

  @Builder
  record MetricKey(
      String spanName,
      String statusCode,
      String spanKind,
      String exception
  ) {

    static MetricKey from(SpanData spanData) {
      return MetricKey
          .builder()
          .spanName(normalizeSpanName(spanData.getName()))
          .statusCode(resolveStatusCode(spanData))
          .spanKind(spanData
              .getKind()
              .name()
              .toLowerCase(Locale.ROOT))
          .exception(mapException(SpanExceptionExtractor.extract(spanData)))
          .build();
    }

    static String mapException(RecordedException op) {
      if (op == null) {
        return "none";
      }
      return op.type();
    }

    static String normalizeSpanName(String spanName) {
      if (spanName == null || spanName.isBlank()) {
        return "unknown";
      }
      return spanName;
    }

    static String resolveStatusCode(SpanData spanData) {
      final var statusCode = spanData
          .getStatus()
          .getStatusCode();
      return statusCode == StatusCode.ERROR
          ? "ERROR"
          : "SUCCESS";
    }
  }
}
