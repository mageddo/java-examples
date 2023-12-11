package com.mageddo.customer.balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerBalanceFactory {
  public static CustomerBalance buildForCreation(UUID customerId, BigDecimal initialAmount) {
    return CustomerBalance
        .builder()
        .customerId(customerId)
        .balance(initialAmount)
        .createdAt(LocalDateTime.now())
        .id(UUID.randomUUID())
        .build();
  }
}
