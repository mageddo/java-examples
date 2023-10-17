package com.mageddo.coffeemaker.checkout;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoffeeCheckoutDomainEventCheckMDB {

  @KafkaListener(
      topics = "coffee_maker_checkout_event_java_agent_app",
      groupId = "app_coffee_maker_checkout_event_java_agent_app"
  )
  public void consume(String msg, @Header("traceparent") String traceParent){
    Span.current().setAttribute("x-h-traceParent", traceParent);
    log.info("consume checked: {}", msg);
  }

}
