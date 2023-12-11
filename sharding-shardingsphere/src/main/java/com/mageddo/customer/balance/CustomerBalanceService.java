package com.mageddo.customer.balance;

import java.math.BigDecimal;
import java.util.UUID;

import com.mageddo.customer.balance.infrastructure.CustomerBalanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerBalanceService {

  public static final int POSITIVE = 1;
  public static final int NEGATIVE = -1;
  private final CustomerBalanceRepository customerBalanceRepository;

  @Autowired
  public CustomerBalanceService(CustomerBalanceRepository customerBalanceRepository) {
    this.customerBalanceRepository = customerBalanceRepository;
  }

  public void debt(UUID customerId, BigDecimal amount) {
    throw new UnsupportedOperationException();
  }

  public void credit(UUID customerId, BigDecimal amount) {
    throw new UnsupportedOperationException();
  }

  public void createAccount(UUID customerId, BigDecimal initialAmount) {
    final var customerBalance = CustomerBalanceFactory.buildForCreation(customerId, initialAmount);
    this.customerBalanceRepository.create(customerBalance);
  }

  public void createAccount(UUID customerId) {
    this.createAccount(customerId, BigDecimal.ZERO);
  }

  public void changeBalance(UUID customerId, BigDecimal amount) {
    if (amount.signum() == POSITIVE) {
      this.credit(customerId, amount);
    } else if (amount.signum() == NEGATIVE) {
      this.debt(customerId, amount.abs());
    } else {
      throw new IllegalArgumentException(String.format("invalid amount: " + amount));
    }
  }
}
