package com.mageddo.investment_product.investment.dataprovider;

import com.mageddo.investment_product.investment.Investment;
import java.util.List;

public interface InvestmentDAO {

  void save(Investment investment);

  Investment findById(String investmentId);

  List<Investment> findByWalletId(String walletId);
}
