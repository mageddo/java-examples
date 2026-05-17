package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Investor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class InvestorDaoPg implements InvestorDAO {

  @Inject
  EntityManager entityManager;

  @Override
  @Transactional
  public void save(Investor investor) {
    this.entityManager.merge(investor);
  }

  @Override
  @Transactional
  public Investor findById(String investorId) {
    return this.entityManager.find(Investor.class, investorId);
  }
}
