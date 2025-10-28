package com.pagbank.logging;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@Slf4j
@SpringBootApplication
public class App implements InitializingBean {
  public static void main(String[] args) {
    MDC.put("traceId", UUID.randomUUID().toString());
    SpringApplication.run(App.class, args);
  }

  @Override
  public void afterPropertiesSet() {
    log.info("action=helloWorld");
  }

  @Bean
  public SpanHandler customSpanHandler() {
    return new SpanHandler() {
      @Override
      public boolean end(TraceContext context, MutableSpan span, Cause cause) {
//        MDC.remove("trace_id");
//        MDC.remove("trace_flags");
//        MDC.remove("span_id");
//        MDC.remove("spanId");
        MDC.put("traceIdx", context.traceIdString().substring(0, 8));
        return true;
      }
    };
  }

}
