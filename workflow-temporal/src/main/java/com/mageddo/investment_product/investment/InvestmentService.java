package com.mageddo.investment_product.investment;

import com.mageddo.investment_product.investment.dataprovider.InvestmentDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InvestmentService {

  private final InvestmentDAO investmentDAO;

  @Transactional
  public void save(Investment investment) {
    this.investmentDAO.save(investment);
  }

  @Transactional
  public Investment findById(String investmentId) {
    return this.investmentDAO.findById(investmentId);
  }

  @Transactional
  public List<Investment> findByWalletId(String walletId) {
    return this.investmentDAO.findByWalletId(walletId);
  }
}
