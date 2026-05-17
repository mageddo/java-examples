package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FinancialEventCandidateDaoPg implements FinancialEventCandidateDAO {

  @Inject
  EntityManager entityManager;

  @Override
  @Transactional
  public void save(FinancialEventCandidate candidate) {
    this.entityManager.merge(candidate);
  }

  @Override
  @Transactional
  public FinancialEventCandidate findById(String candidateId) {
    return this.entityManager.find(FinancialEventCandidate.class, candidateId);
  }

  @Override
  @Transactional
  public List<FinancialEventCandidate> findByWalletId(String walletId) {
    return this.entityManager.createNativeQuery(
        """
          select candidate.*
          from financial_event_candidate candidate
          join investment investment on investment.id = candidate.investment_id
          where investment.wallet_id = :walletId
          order by candidate.id
          """,
        FinancialEventCandidate.class)
      .setParameter("walletId", walletId)
      .getResultList();
  }
}
