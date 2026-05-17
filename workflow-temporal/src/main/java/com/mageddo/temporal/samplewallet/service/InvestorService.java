package com.mageddo.temporal.samplewallet.service;

import com.mageddo.temporal.samplewallet.dataprovider.InvestorDAO;
import com.mageddo.temporal.samplewallet.domain.Investor;
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
