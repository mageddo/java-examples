package com.mageddo.coffeemaker.checkout;

import io.micrometer.core.annotation.Timed;

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
  @Timed(
      value = "duration.ms",
      serviceLevelObjectives = {0.05, 0.1, 1},
      extraTags = {
          "span_name", "CoffeeCheckoutDomainEventSenderKafka.send",
          "span_kind", "INTERNAL"
      }
  )
  public void send(CoffeeCheckoutReq req) {
    this.kafkaTemplate.send("coffee_maker_checkout_event", req.toString());
  }
}
