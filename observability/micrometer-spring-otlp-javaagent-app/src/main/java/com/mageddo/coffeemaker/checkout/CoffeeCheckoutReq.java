package com.mageddo.coffeemaker.checkout;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CoffeeCheckoutReq {
  private String coffeeName;
  private BigDecimal amount;

  public CoffeeCheckout toCheckout() {
    return CoffeeCheckout
        .builder()
        .name(this.coffeeName)
        .amount(this.amount)
        .id(UUID.randomUUID())
        .build()
        ;
  }
}
