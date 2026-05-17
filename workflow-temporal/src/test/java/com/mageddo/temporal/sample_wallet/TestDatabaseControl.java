package com.mageddo.temporal.sample_wallet;

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
    this.entityManager.createNativeQuery("DELETE FROM INV.FINANCIAL_EVENT_CANDIDATE").executeUpdate();
    this.entityManager.createNativeQuery("DELETE FROM INV.INVESTMENT").executeUpdate();
    this.entityManager.createNativeQuery("DELETE FROM INV.WALLET").executeUpdate();
    this.entityManager.createNativeQuery("DELETE FROM INV.INVESTOR").executeUpdate();
  }
}
