package com.mageddo.temporal.samplewallet;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TestDatabaseControl {

  final EntityManager entityManager;

  @Transactional
  public void clear() {
    this.entityManager.createNativeQuery("DELETE FROM FINANCIAL_EVENT_CANDIDATE").executeUpdate();
    this.entityManager.createNativeQuery("DELETE FROM INVESTMENT").executeUpdate();
    this.entityManager.createNativeQuery("DELETE FROM WALLET").executeUpdate();
    this.entityManager.createNativeQuery("DELETE FROM INVESTOR").executeUpdate();
  }
}
