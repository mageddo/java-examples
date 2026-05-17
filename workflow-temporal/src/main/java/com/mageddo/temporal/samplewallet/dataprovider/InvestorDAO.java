package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Investor;

public interface InvestorDAO {

  void save(Investor investor);

  Investor findById(String investorId);
}
