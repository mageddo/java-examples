package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Investor;
import org.hibernate.SessionFactory;

public class InvestorRepo {

  private final TransactionRunner transactionRunner;

  public InvestorRepo(SessionFactory sessionFactory) {
    this.transactionRunner = new TransactionRunner(sessionFactory);
  }

  public void save(Investor investor) {
    this.transactionRunner.requiredVoid(session -> session.merge(investor));
  }

  public Investor findById(String investorId) {
    return this.transactionRunner.required(session -> session.find(Investor.class, investorId));
  }
}
