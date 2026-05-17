package com.mageddo.investment_product.financial_event_candidate.dataprovider;

import com.mageddo.investment_product.financial_event_candidate.FinancialEventCandidate;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class FinancialEventCandidateDaoPg implements FinancialEventCandidateDAO {

  final EntityManager entityManager;

  @Override
  public void save(FinancialEventCandidate candidate) {
    this.entityManager.merge(candidate);
  }

  @Override
  public FinancialEventCandidate findById(String candidateId) {
    return this.entityManager.find(FinancialEventCandidate.class, candidateId);
  }

  @Override
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
