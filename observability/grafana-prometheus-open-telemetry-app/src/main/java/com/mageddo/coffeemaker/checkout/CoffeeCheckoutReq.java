package com.mageddo.coffeemaker.checkout;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CoffeeCheckoutReq {
  private String coffeeName;
  private BigDecimal amount;
}
