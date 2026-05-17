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
          from FINANCIAL_EVENT_CANDIDATE candidate
          join INVESTMENT investment on investment.IDT_INVESTMENT = candidate.IDT_INVESTMENT
          where investment.IDT_WALLET = :walletId
          order by candidate.IDT_FINANCIAL_EVENT_CANDIDATE
          """,
        FinancialEventCandidate.class)
      .setParameter("walletId", walletId)
      .getResultList();
  }
}
