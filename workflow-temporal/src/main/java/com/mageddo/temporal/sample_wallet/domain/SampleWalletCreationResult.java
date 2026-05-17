package com.mageddo.temporal.sample_wallet.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SampleWalletCreationResult(
  @NonNull String walletId,
  @NonNull List<String> investmentIds,
  @NonNull List<String> candidateIds
) {
}
