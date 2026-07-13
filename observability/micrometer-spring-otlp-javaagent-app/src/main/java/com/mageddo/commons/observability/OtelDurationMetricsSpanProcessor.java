package com.mageddo.commons.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.SpanData;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public final class OtelDurationMetricsSpanProcessor implements SpanProcessor {

  public static final String METRIC_NAME = "otel_duration_ms";

  private static final Duration[] BUCKETS = {
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

  /*
   * Evita executar Timer.builder(...).register(...) no encerramento
   * de todo span.
   */
  private final ConcurrentMap<MetricKey, Timer> timers =
      new ConcurrentHashMap<>();

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
      final var spanData = span.toSpanData();
      final var durationNanos =
          spanData.getEndEpochNanos() - spanData.getStartEpochNanos();

      if (durationNanos < 0) {
        return;
      }

      final var key = MetricKey.from(spanData);
      final var timer = this.timers.computeIfAbsent(
          key,
          this::createTimer
      );

      timer.record(durationNanos, TimeUnit.NANOSECONDS);
    } catch (RuntimeException ignored) {
      /*
       * Telemetria nunca deve interromper a execução da aplicação.
       * Evite logar aqui para não criar recursão ou ruído por span.
       */
    }
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

  private Timer createTimer(MetricKey key) {
    return Timer.builder(METRIC_NAME)
        .description("Duration of completed OpenTelemetry spans")
        .tags(
            "span_name", key.spanName(),
            "status_code", key.statusCode(),
            "span_kind", key.spanKind()
        )
        /*
         * serviceLevelObjectives cria os buckets cumulativos
         * explicitamente definidos.
         *
         * Não usar publishPercentileHistogram(), pois ele adicionaria
         * os buckets predefinidos do Micrometer.
         */
        .serviceLevelObjectives(BUCKETS)
        .register(this.meterRegistry);
  }

  private record MetricKey(
      String spanName,
      String statusCode,
      String spanKind
  ) {

    private static MetricKey from(SpanData spanData) {
      return new MetricKey(
          normalizeSpanName(spanData.getName()),
          resolveStatusCode(spanData),
          spanData.getKind().name().toLowerCase(Locale.ROOT)
      );
    }

    private static String normalizeSpanName(String spanName) {
      if (spanName == null || spanName.isBlank()) {
        return "unknown";
      }
      return spanName;
    }

    private static String resolveStatusCode(SpanData spanData) {
      return spanData.getStatus().getStatusCode() == StatusCode.ERROR
          ? "error"
          : "success";
    }
  }
}
