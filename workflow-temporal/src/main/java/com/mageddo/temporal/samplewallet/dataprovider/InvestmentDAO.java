package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Investment;
import java.util.List;

public interface InvestmentDAO {

  void save(Investment investment);

  Investment findById(String investmentId);

  List<Investment> findByWalletId(String walletId);
}
