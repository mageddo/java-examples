package com.mageddo.coffeemaker.checkout;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class CoffeeCheckoutController {

  private final CoffeeCheckoutService coffeeCheckoutService;

  @PostMapping("/checkout")
  public void checkout(CoffeeCheckoutReq c){
    this.coffeeCheckoutService.checkout(c);
  }

  @GetMapping("/ping")
  public String ping(){
    return LocalDateTime.now().toString();
  }
}
