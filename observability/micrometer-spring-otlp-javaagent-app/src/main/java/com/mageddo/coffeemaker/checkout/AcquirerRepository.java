package com.mageddo.coffeemaker.checkout;

public interface AcquirerRepository {
  void processPayment(CoffeeCheckoutReq req);
}
