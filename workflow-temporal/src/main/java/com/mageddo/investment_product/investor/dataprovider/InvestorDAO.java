package com.mageddo.investment_product.investor.dataprovider;

import com.mageddo.investment_product.investor.Investor;

public interface InvestorDAO {

  void save(Investor investor);

  Investor findById(String investorId);
}
