package com.mageddo.temporal.sample_wallet.domain;

import com.mageddo.investment_product.financial_event_candidate.CandidateStatus;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CandidateProcessedSignal(
  @NonNull String candidateId,
  @NonNull CandidateStatus status,
  boolean processed
) {
}
