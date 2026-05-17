package com.mageddo.investment_product.investor.dataprovider;

import com.mageddo.investment_product.investor.Investor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InvestorDaoPg implements InvestorDAO {

  final EntityManager entityManager;

  @Override
  public void save(Investor investor) {
    this.entityManager.merge(investor);
  }

  @Override
  public Investor findById(String investorId) {
    return this.entityManager.find(Investor.class, investorId);
  }
}
