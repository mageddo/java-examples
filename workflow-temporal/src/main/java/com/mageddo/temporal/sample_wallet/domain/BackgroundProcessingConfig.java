package com.mageddo.temporal.sample_wallet.domain;

import java.time.Duration;
import java.util.Set;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BackgroundProcessingConfig {

  @NonNull
  Duration investmentCreationDelay;

  @NonNull
  Duration candidateProcessingDelay;

  @NonNull
  Set<String> failFirstAttemptCandidateIds;

  @NonNull
  Set<String> alwaysSlowCandidateIds;

  int maxCandidateAttempts;

  public static BackgroundProcessingConfig defaults() {
    return BackgroundProcessingConfig.builder()
      .investmentCreationDelay(Duration.ofMillis(50))
      .candidateProcessingDelay(Duration.ofMillis(50))
      .failFirstAttemptCandidateIds(Set.of())
      .alwaysSlowCandidateIds(Set.of())
      .maxCandidateAttempts(2)
      .build();
  }
}
