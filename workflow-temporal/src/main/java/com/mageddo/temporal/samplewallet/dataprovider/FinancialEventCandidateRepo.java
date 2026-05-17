package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate;
import java.util.List;
import org.hibernate.SessionFactory;

public class FinancialEventCandidateRepo {

  private final TransactionRunner transactionRunner;

  public FinancialEventCandidateRepo(SessionFactory sessionFactory) {
    this.transactionRunner = new TransactionRunner(sessionFactory);
  }

  public void save(FinancialEventCandidate candidate) {
    this.transactionRunner.requiredVoid(session -> session.merge(candidate));
  }

  public FinancialEventCandidate findById(String candidateId) {
    return this.transactionRunner.required(session -> session.find(FinancialEventCandidate.class, candidateId));
  }

  public List<FinancialEventCandidate> findByWalletId(String walletId) {
    return this.transactionRunner.required(session -> session.createNativeQuery(
        """
          select candidate.*
          from financial_event_candidate candidate
          join investment investment on investment.id = candidate.investment_id
          where investment.wallet_id = :walletId
          order by candidate.id
          """,
        FinancialEventCandidate.class)
      .setParameter("walletId", walletId)
      .getResultList());
  }
}
