package com.mageddo.investment_product.financial_event_candidate.dataprovider;

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
          SELECT FEC.*
          FROM FINANCIAL_EVENT_CANDIDATE FEC
          INNER JOIN INVESTMENT I
            ON I.IDT_INVESTMENT = FEC.IDT_INVESTMENT
          WHERE I.IDT_WALLET = :walletId
          ORDER BY FEC.IDT_FINANCIAL_EVENT_CANDIDATE
          """,
        FinancialEventCandidate.class)
      .setParameter("walletId", walletId)
      .getResultList();
  }
}
