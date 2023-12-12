package com.mageddo.shardingsphere.customer.balance.infrastructure;

import java.math.BigDecimal;
import java.util.UUID;

import javax.sql.DataSource;

import com.mageddo.shardingsphere.customer.balance.CustomerBalance;
import com.mageddo.shardingsphere.customer.balance.CustomerBalanceHistory;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class CustomerBalanceRepositoryImpl implements CustomerBalanceRepository {

  private final EntityManager entityManager;
  private final DataSource dataSource;

  @Autowired
  public CustomerBalanceRepositoryImpl(EntityManager entityManager, DataSource dataSource) {
    this.entityManager = entityManager;
    this.dataSource = dataSource;
  }

  @Override
  public void create(CustomerBalance customerBalance) {
    this.entityManager.persist(customerBalance);
    this.entityManager.persist(CustomerBalanceHistory.of(customerBalance));
  }

  @Override
  public void debt(UUID customerId, BigDecimal amount) {
    final var updated = this.entityManager.createNativeQuery("""
            UPDATE CUSTOMER_BALANCE SET
              NUM_AMOUNT = NUM_AMOUNT - :amount
            WHERE COD_CUSTOMER  = :customerId
            AND NUM_AMOUNT - :amount >= 0
            """)
        .executeUpdate();
    Validate.isTrue(
        updated == 1,
        "Failed to debt customerId=%s, debtAmount=%s",
        customerId, amount
    );
  }

  @Override
  public void credit(UUID customerId, BigDecimal amount) {
    final var updated = this.entityManager.createNativeQuery("""
            UPDATE CUSTOMER_BALANCE SET
              NUM_AMOUNT = NUM_AMOUNT + :amount
            WHERE COD_CUSTOMER  = :customerId
            """)
        .executeUpdate();
    Validate.isTrue(
        updated == 1,
        "Failed to credit customerId=%s, debtAmount=%s",
        customerId, amount
    );
  }
}
