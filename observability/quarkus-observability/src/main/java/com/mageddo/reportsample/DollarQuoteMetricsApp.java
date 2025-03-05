package com.mageddo.reportsample;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;

@Singleton
public class DollarQuoteMetricsApp {

  private static final SecureRandom r = new SecureRandom();
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @WithSpan
  @Scheduled(every = "5s")
  @Timed("dollar_quote_job_quarkus_scheduler")
  public void dollarQuoteJob() throws Exception {
    log.info("status=jobRan");
    Thread.sleep(300);
  }

  public void currentTimeMetricGaugeCollector(@Observes StartupEvent e) {
    Gauge
        .builder("dollar_quote_micrometer_gauge", () -> {
          final var amount = this.checkDollarQuote();
          log.info("status=calculated, amount={}", amount);
          return amount;
        })
        .baseUnit("number")
        .tag("version_tag", "v1")
        .register(Metrics.globalRegistry)
    ;
    log.info("status=gaugeRegistered");
  }

  private  double checkDollarQuote() {
    return BigDecimal.valueOf(r.nextDouble() + 5.0)
        .setScale(2, RoundingMode.HALF_UP)
        .doubleValue();
  }

}
