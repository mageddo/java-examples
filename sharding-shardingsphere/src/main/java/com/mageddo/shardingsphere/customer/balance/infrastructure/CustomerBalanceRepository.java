package com.mageddo.shardingsphere.customer.balance.infrastructure;

import java.math.BigDecimal;
import java.util.UUID;

import com.mageddo.shardingsphere.customer.balance.CustomerBalance;

public interface CustomerBalanceRepository {

  void create(CustomerBalance customerBalance);

  void debt(UUID customerId, BigDecimal amount);

  void credit(UUID customerId, BigDecimal amount);

}
