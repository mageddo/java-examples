package com.mageddo.investment_product.wallet.dataprovider;

import com.mageddo.investment_product.wallet.Wallet;
import java.util.List;

public interface WalletDAO {

  void save(Wallet wallet);

  Wallet findById(String walletId);

  List<Wallet> findByInvestorId(String investorId);
}
