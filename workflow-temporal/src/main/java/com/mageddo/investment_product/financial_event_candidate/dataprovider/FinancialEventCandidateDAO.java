package com.mageddo.investment_product.financial_event_candidate.dataprovider;

import com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate;
import java.util.List;

public interface FinancialEventCandidateDAO {

  void save(FinancialEventCandidate candidate);

  FinancialEventCandidate findById(String candidateId);

  List<FinancialEventCandidate> findByWalletId(String walletId);
}
