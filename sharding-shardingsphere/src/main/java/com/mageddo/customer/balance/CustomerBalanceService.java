package com.mageddo.customer.balance;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerBalanceService {

  private final CustomerBalanceRepository customerBalanceRepository;

  @Autowired
  public CustomerBalanceService(CustomerBalanceRepository customerBalanceRepository) {
    this.customerBalanceRepository = customerBalanceRepository;
  }

  public void debt(UUID customerId, BigDecimal amount){
    throw new UnsupportedOperationException();
  }

  public void credit(UUID customerId, BigDecimal amount){
    throw new UnsupportedOperationException();
  }

  public void createAccount(UUID customerId, BigDecimal initialAmount){
    final var customerBalance = CustomerBalanceFactory.buildForCreation(customerId, initialAmount);
    this.customerBalanceRepository.create(customerBalance);
  }
}
