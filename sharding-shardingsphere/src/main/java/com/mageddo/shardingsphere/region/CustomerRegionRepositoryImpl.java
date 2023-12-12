package com.mageddo.shardingsphere.region;

import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomerRegionRepositoryImpl implements CustomerRegionRepository {

  private final EntityManager entityManager;
  private final NamedParameterJdbcTemplate jdbcTemplate;

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

  @Override
  public void create(CustomerRegion region) {
    final var affected = this.jdbcTemplate.update("""
            INSERT INTO CUSTOMER_REGION (
              IDT_CUSTOMER_REGION, NAM_REGION, COD_CUSTOMER
            ) VALUES (
              :id, :region, :customerId
            )
            """,
        new MapSqlParameterSource()
            .addValue("id", region.getId())
            .addValue("region", region.getName())
            .addValue("customerId", region.getCustomerId())
    );
    Validate.isTrue(affected == 1, "failed to insert customer region %s", region.getId());
  }
}
