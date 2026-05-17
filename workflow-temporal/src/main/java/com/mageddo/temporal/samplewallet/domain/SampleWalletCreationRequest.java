package com.mageddo.temporal.samplewallet.domain;

import java.time.Duration;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SampleWalletCreationRequest(
  @NonNull String investorId,
  @NonNull Duration workflowTimeout
) {
}
