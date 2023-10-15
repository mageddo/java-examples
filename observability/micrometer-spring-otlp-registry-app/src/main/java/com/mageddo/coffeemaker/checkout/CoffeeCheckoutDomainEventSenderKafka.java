package com.mageddo.coffeemaker.checkout;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class CoffeeCheckoutDomainEventSenderKafka implements CoffeeCheckoutDomainEventSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Override
  @SneakyThrows
  public void send(CoffeeCheckoutReq req) {
    this.kafkaTemplate.send("coffee_maker_checkout_event", req.toString()).get();
  }
}
