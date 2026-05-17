package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Wallet;
import java.util.List;
import org.hibernate.SessionFactory;

public class WalletRepo {

  private final TransactionRunner transactionRunner;

  public WalletRepo(SessionFactory sessionFactory) {
    this.transactionRunner = new TransactionRunner(sessionFactory);
  }

  public void save(Wallet wallet) {
    this.transactionRunner.requiredVoid(session -> session.merge(wallet));
  }

  public Wallet findById(String walletId) {
    return this.transactionRunner.required(session -> session.find(Wallet.class, walletId));
  }

  public List<Wallet> findByInvestorId(String investorId) {
    return this.transactionRunner.required(session -> session.createNativeQuery(
        """
          select *
          from wallet
          where investor_id = :investorId
          order by created_at
          """,
        Wallet.class)
      .setParameter("investorId", investorId)
      .getResultList());
  }
}
