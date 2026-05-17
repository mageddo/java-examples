package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Wallet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class WalletDaoPg implements WalletDAO {

  @Inject
  EntityManager entityManager;

  @Override
  @Transactional
  public void save(Wallet wallet) {
    this.entityManager.merge(wallet);
  }

  @Override
  @Transactional
  public Wallet findById(String walletId) {
    return this.entityManager.find(Wallet.class, walletId);
  }

  @Override
  @Transactional
  public List<Wallet> findByInvestorId(String investorId) {
    return this.entityManager.createNativeQuery(
        """
          select *
          from WALLET
          where IDT_INVESTOR = :investorId
          order by DAT_CREATED
          """,
        Wallet.class)
      .setParameter("investorId", investorId)
      .getResultList();
  }
}
