package com.mageddo.customer.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class CustomerBalanceRepositoryImpl implements CustomerBalanceRepository {

  private final EntityManager entityManager;

  @Autowired
  public CustomerBalanceRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void create(CustomerBalance customerBalance) {
    this.entityManager.persist(customerBalance);
  }
}
