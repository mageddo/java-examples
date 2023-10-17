package com.mageddo.coffeemaker.checkout;

public interface CoffeeCheckoutDAO {

  void save(CoffeeCheckout checkout);

  Long countCheckoutsByName(String name);
}
