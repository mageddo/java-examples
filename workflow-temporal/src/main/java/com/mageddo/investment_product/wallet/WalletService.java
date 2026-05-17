package com.mageddo.investment_product.wallet;

import com.mageddo.investment_product.wallet.dataprovider.WalletDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WalletService {

  final WalletDAO walletDAO;

  @Transactional
  public void save(Wallet wallet) {
    this.walletDAO.save(wallet);
  }

  @Transactional
  public Wallet findById(String walletId) {
    return this.walletDAO.findById(walletId);
  }

  @Transactional
  public List<Wallet> findByInvestorId(String investorId) {
    return this.walletDAO.findByInvestorId(investorId);
  }
}
