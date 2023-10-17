package com.mageddo.coffeemaker.checkout;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeCheckoutDAOImpl implements CoffeeCheckoutDAO {

  private final EntityManager entityManager;

  @Override
  public void save(CoffeeCheckout checkout) {
    this.entityManager.merge(checkout);
  }
}
