package com.mageddo.temporal.sample_wallet.domain.templates;

import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationRequest;
import java.time.Duration;

public class SampleWalletCreationRequestTemplates {

  public static SampleWalletCreationRequest defaultRequest(String investorId) {
    return SampleWalletCreationRequest.builder()
      .investorId(investorId)
      .workflowTimeout(Duration.ofSeconds(5))
      .build();
  }

  public static SampleWalletCreationRequest timeoutRequest(String investorId) {
    return SampleWalletCreationRequest.builder()
      .investorId(investorId)
      .workflowTimeout(Duration.ofMillis(200))
      .build();
  }
}
