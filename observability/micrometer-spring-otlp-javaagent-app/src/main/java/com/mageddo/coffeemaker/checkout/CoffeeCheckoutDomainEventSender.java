package com.mageddo.coffeemaker.checkout;

public interface CoffeeCheckoutDomainEventSender {
  void send(CoffeeCheckoutReq req);
}
