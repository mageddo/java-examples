package com.mageddo.temporal.samplewallet.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record CandidateProcessedSignal(
  @NonNull String candidateId,
  @NonNull CandidateStatus status,
  boolean processed
) {
}
