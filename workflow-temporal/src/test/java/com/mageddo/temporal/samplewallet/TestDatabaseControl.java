package com.mageddo.temporal.samplewallet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TestDatabaseControl {

  @Inject
  EntityManager entityManager;

  @Transactional
  public void clear() {
    this.entityManager.createNativeQuery("delete from financial_event_candidate").executeUpdate();
    this.entityManager.createNativeQuery("delete from investment").executeUpdate();
    this.entityManager.createNativeQuery("delete from wallet").executeUpdate();
    this.entityManager.createNativeQuery("delete from investor").executeUpdate();
  }
}
