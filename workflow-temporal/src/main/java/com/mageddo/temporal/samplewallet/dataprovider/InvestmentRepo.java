package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Investment;
import java.util.List;
import org.hibernate.SessionFactory;

public class InvestmentRepo {

  private final TransactionRunner transactionRunner;

  public InvestmentRepo(SessionFactory sessionFactory) {
    this.transactionRunner = new TransactionRunner(sessionFactory);
  }

  public void save(Investment investment) {
    this.transactionRunner.requiredVoid(session -> session.merge(investment));
  }

  public Investment findById(String investmentId) {
    return this.transactionRunner.required(session -> session.find(Investment.class, investmentId));
  }

  public List<Investment> findByWalletId(String walletId) {
    return this.transactionRunner.required(session -> session.createNativeQuery(
        """
          select *
          from investment
          where wallet_id = :walletId
          order by id
          """,
        Investment.class)
      .setParameter("walletId", walletId)
      .getResultList());
  }
}
