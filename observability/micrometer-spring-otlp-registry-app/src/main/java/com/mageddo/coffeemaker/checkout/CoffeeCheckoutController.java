package com.mageddo.coffeemaker.checkout;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.tracing.annotation.NewSpan;


@RestController
@RequestMapping("/api/v1/")
public class CoffeeCheckoutController {

  private final CoffeeCheckoutService coffeeCheckoutService;

  @Autowired
  public CoffeeCheckoutController(CoffeeCheckoutService coffeeCheckoutService) {
    this.coffeeCheckoutService = coffeeCheckoutService;
  }

  @PostMapping("/checkout")
  public void checkout(CoffeeCheckoutReq c){
    this.coffeeCheckoutService.checkout(c);
  }

  @GetMapping("/ping")
  public String ping(){
    return LocalDateTime.now().toString();
  }
}
