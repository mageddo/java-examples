package com.mageddo.coffeemaker.checkout;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoffeeCheckoutDomainEventSenderKafka implements CoffeeCheckoutDomainEventSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @WithSpan
  @Override
  public void send(CoffeeCheckout req) {
    this.kafkaTemplate.send("coffee_maker_checkout_event_java_agent_app", req.toString());
  }
}
