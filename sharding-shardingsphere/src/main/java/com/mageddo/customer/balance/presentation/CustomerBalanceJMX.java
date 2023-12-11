package com.mageddo.customer.balance.presentation;

import java.math.BigDecimal;
import java.util.UUID;

import com.mageddo.customer.balance.CustomerBalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class CustomerBalanceJMX {

  private final CustomerBalanceService customerBalanceService;

  @Autowired
  public CustomerBalanceJMX(CustomerBalanceService customerBalanceService) {
    this.customerBalanceService = customerBalanceService;
  }

  @ManagedOperation
  public void create(String customerId) {
    this.customerBalanceService.createAccount(UUID.fromString(customerId));
  }

  @ManagedOperation
  public void changeBalance(String customerId, String amount) {
    this.customerBalanceService.changeBalance(UUID.fromString(customerId), new BigDecimal(amount));
  }
}
