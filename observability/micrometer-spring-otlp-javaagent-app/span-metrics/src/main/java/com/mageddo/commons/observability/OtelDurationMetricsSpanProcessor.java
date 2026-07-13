package com.mageddo.commons.observability;

import com.mageddo.commons.observability.SpanExceptionExtractor.RecordedException;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.SpanData;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SpanProcessor de extensão do OpenTelemetry Java Agent que transforma a duração dos spans
 * finalizados em um histograma de métricas, exportado pelo pipeline OTLP do próprio agent.
 */
public final class OtelDurationMetricsSpanProcessor implements SpanProcessor {

  private static final Logger logger =
      Logger.getLogger(OtelDurationMetricsSpanProcessor.class.getName());

  public static final String METRIC_NAME = "custom_duration_ms";

  private static final String INSTRUMENTATION_SCOPE = "observability";

  static final List<Double> BUCKETS = Arrays.asList(
      1.0,
      2.0,
      4.0,
      5.0,
      8.0,
      10.0,
      30.0,
      50.0
  );

  private volatile DoubleHistogram histogram;

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
    } catch (RuntimeException e) {
      logger.log(
          Level.WARNING,
          e,
          () -> "status=failed, msg=" + e.getMessage() + ", type=" + e.getClass().getSimpleName()
      );
    }
  }

  void record(ReadableSpan span) {

    final var spanData = span.toSpanData();
    final var durationMs =
        (spanData.getEndEpochNanos() - spanData.getStartEpochNanos()) / 1_000_000.0;

    if (durationMs <= 0) {
      return;
    }

    this.histogram().record(durationMs, buildAttributes(spanData));
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
    return CompletableResultCode.ofSuccess();
  }

  private DoubleHistogram histogram() {
    var local = this.histogram;
    if (local == null) {
      synchronized (this) {
        local = this.histogram;
        if (local == null) {
          local = this.createHistogram();
          this.histogram = local;
        }
      }
    }
    return local;
  }

  private DoubleHistogram createHistogram() {
    return GlobalOpenTelemetry
        .getMeter(INSTRUMENTATION_SCOPE)
        .histogramBuilder(METRIC_NAME)
        .setUnit("ms")
        .setDescription("Duration of completed OpenTelemetry spans")
        .setExplicitBucketBoundariesAdvice(BUCKETS)
        .build();
  }

  static Attributes buildAttributes(SpanData spanData) {
    return Attributes
        .builder()
        .put("span_name", normalizeSpanName(spanData.getName()))
        .put("status_code", resolveStatusCode(spanData))
        .put("span_kind", spanData
            .getKind()
            .name()
            .toLowerCase(Locale.ROOT))
        .put("exception", mapException(SpanExceptionExtractor.extract(spanData)))
        .build();
  }

  static String mapException(RecordedException recordedException) {
    if (recordedException == null) {
      return "none";
    }
    return recordedException.getType();
  }

  static String normalizeSpanName(String spanName) {
    if (spanName == null || spanName.trim().isEmpty()) {
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
