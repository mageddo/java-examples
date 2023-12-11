package com.mageddo.customer.balance.infrastructure;

import com.mageddo.customer.balance.CustomerBalance;

public interface CustomerBalanceRepository {
  void create(CustomerBalance customerBalance);
}
