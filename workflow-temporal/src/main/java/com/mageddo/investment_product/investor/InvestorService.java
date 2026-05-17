package com.mageddo.investment_product.investor;

import com.mageddo.investment_product.investor.dataprovider.InvestorDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InvestorService {

  final InvestorDAO investorDAO;

  @Transactional
  public void save(Investor investor) {
    this.investorDAO.save(investor);
  }

  @Transactional
  public Investor findById(String investorId) {
    return this.investorDAO.findById(investorId);
  }
}
