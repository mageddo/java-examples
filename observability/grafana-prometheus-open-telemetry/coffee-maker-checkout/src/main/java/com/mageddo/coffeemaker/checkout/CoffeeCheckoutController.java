package com.mageddo.coffeemaker.checkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentelemetry.instrumentation.annotations.WithSpan;

@RestController
public class CoffeeCheckoutController {

  private final CoffeeCheckoutService coffeeCheckoutService;

  @Autowired
  public CoffeeCheckoutController(CoffeeCheckoutService coffeeCheckoutService) {
    this.coffeeCheckoutService = coffeeCheckoutService;
  }

  @WithSpan
  @PostMapping
  public void checkout(CoffeeCheckoutReq c){
    this.coffeeCheckoutService.checkout(c);
  }
}
