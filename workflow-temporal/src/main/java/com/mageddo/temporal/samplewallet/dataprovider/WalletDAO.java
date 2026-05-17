package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Wallet;
import java.util.List;

public interface WalletDAO {

  void save(Wallet wallet);

  Wallet findById(String walletId);

  List<Wallet> findByInvestorId(String investorId);
}
