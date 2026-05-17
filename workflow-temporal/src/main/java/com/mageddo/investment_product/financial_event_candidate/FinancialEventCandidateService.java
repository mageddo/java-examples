package com.mageddo.investment_product.financial_event_candidate;

import com.mageddo.investment_product.financial_event_candidate.dataprovider.FinancialEventCandidateDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class FinancialEventCandidateService {

  final FinancialEventCandidateDAO financialEventCandidateDAO;

  @Transactional
  public void save(FinancialEventCandidate candidate) {
    this.financialEventCandidateDAO.save(candidate);
  }

  @Transactional
  public FinancialEventCandidate findById(String candidateId) {
    return this.financialEventCandidateDAO.findById(candidateId);
  }

  @Transactional
  public List<FinancialEventCandidate> findByWalletId(String walletId) {
    return this.financialEventCandidateDAO.findByWalletId(walletId);
  }
}
