package com.mageddo.coffeemaker.checkout;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CoffeeCheckoutReq {
  private String coffeeName;
  private BigDecimal amount;
}
