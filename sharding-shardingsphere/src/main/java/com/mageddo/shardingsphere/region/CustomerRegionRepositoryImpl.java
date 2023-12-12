package com.mageddo.shardingsphere.region;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomerRegionRepositoryImpl implements CustomerRegionRepository {

  private final EntityManager entityManager;

  @Override
  public CustomerRegion findByCustomerId(UUID customerId) {
    try {
      return (CustomerRegion) this.entityManager.createNativeQuery("""
          SELECT * FROM CUSTOMER_REGION WHERE COD_CUSTOMER = :customerId
          """, CustomerRegion.class)
          .setParameter("customerId", customerId)
          .getSingleResult()
          ;
    } catch (NoResultException e) {
      return null;
    }
  }
}
