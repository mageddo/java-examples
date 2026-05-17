package com.mageddo.investment_product.wallet.dataprovider;

import com.mageddo.investment_product.wallet.Wallet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WalletDaoPg implements WalletDAO {

  final EntityManager entityManager;

  @Override
  public void save(Wallet wallet) {
    this.entityManager.merge(wallet);
  }

  @Override
  public Wallet findById(String walletId) {
    return this.entityManager.find(Wallet.class, walletId);
  }

  @Override
  public List<Wallet> findByInvestorId(String investorId) {
    return this.entityManager.createNativeQuery(
        """
          SELECT *
          FROM INV.WALLET
          WHERE IDT_INVESTOR = :investorId
          ORDER BY DAT_CREATED
          """,
        Wallet.class)
      .setParameter("investorId", investorId)
      .getResultList();
  }
}
