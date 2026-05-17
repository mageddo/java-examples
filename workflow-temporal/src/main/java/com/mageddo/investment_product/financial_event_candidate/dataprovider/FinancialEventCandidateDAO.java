package com.mageddo.investment_product.financial_event_candidate.dataprovider;

import com.mageddo.investment_product.financial_event_candidate.FinancialEventCandidate;
import java.util.List;

public interface FinancialEventCandidateDAO {

  void save(FinancialEventCandidate candidate);

  FinancialEventCandidate findById(String candidateId);

  List<FinancialEventCandidate> findByWalletId(String walletId);
}
