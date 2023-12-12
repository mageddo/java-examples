package com.mageddo.shardingsphere.customer.balance.presentation;

import java.math.BigDecimal;
import java.util.UUID;

import com.mageddo.shardingsphere.customer.balance.CustomerBalanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    try {
      this.customerBalanceService.createAccount(UUID.fromString(customerId));
    } catch (Exception e){
      log.error("status=failed-to-create", e);
    }
  }

  @ManagedOperation
  public void changeBalance(String customerId, String amount) {
    try {
      this.customerBalanceService.changeBalance(UUID.fromString(customerId), new BigDecimal(amount));
    } catch (Exception e){
      log.error("status=failed-to-change-balance", e);
    }
  }
}
