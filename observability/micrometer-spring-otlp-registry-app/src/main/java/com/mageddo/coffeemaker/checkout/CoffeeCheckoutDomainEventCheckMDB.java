package com.mageddo.coffeemaker.checkout;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.trace.Span;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoffeeCheckoutDomainEventCheckMDB {

  @KafkaListener(
      topics = "coffee_maker_checkout_event", groupId = "app_coffee_maker_checkout_event",
      containerFactory = "facZX"
  )
  public void consume(String msg, @Header("traceparent") String traceParent){
    Span.current().setAttribute("x-h-traceParent", traceParent);
    log.info("consume checked: {}", msg);
  }

  @org.springframework.context.annotation.Configuration
  static class Configuration {
    @Bean
    ConcurrentKafkaListenerContainerFactory facZX(ConsumerFactory cf){
      final var factory = new ConcurrentKafkaListenerContainerFactory<>();
      final var props = factory.getContainerProperties();
      props.setObservationEnabled(true); // << necessary
      factory.setConsumerFactory(cf);
      return factory;
    }
  }
}
