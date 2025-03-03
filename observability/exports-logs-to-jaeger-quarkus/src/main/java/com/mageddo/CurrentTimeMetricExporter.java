package com.mageddo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class CurrentTimeMetricExporter {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @WithSpan
  @Scheduled(every = "5s")
  @Timed("job_process_time")
  public void currentJob(){
    log.info("status=jobRan");
    Gauge
        .builder("current_time_job", () -> {
          log.info("status=calculating");
          return System.currentTimeMillis();
        })
        .baseUnit("number")
        .tag("version_tag", "v1")
        .register(Metrics.globalRegistry)
    ;
  }
}
