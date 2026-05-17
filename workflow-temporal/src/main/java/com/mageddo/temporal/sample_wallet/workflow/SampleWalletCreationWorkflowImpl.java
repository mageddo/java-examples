package com.mageddo.temporal.sample_wallet.workflow;

import com.mageddo.temporal.sample_wallet.activity.SampleWalletCreationActivities;
import com.mageddo.investment_product.financial_event_candidate.CandidateStatus;
import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationRequest;
import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationResult;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class SampleWalletCreationWorkflowImpl implements SampleWalletCreationWorkflow {

  private final SampleWalletCreationActivities activities = Workflow.newActivityStub(
    SampleWalletCreationActivities.class,
    ActivityOptions.newBuilder()
      .setStartToCloseTimeout(Duration.ofSeconds(10))
      .setRetryOptions(RetryOptions.newBuilder()
        .setMaximumAttempts(3)
        .build())
      .build()
  );

  private final Map<String, CandidateStatus> candidateStatuses = new LinkedHashMap<>();
  private final Map<String, Boolean> processedCandidates = new LinkedHashMap<>();

  @Override
  public SampleWalletCreationResult create(SampleWalletCreationRequest request) {
    var walletId = this.activities.createWallet(request.investorId());
    var investmentIds = this.activities.createInvestments(request.investorId(), walletId);
    var candidateIds = this.activities.createFinancialEventCandidates(Workflow.getInfo().getWorkflowId(), walletId, investmentIds);
    this.activities.fanOutCandidates(candidateIds);
    var timedOut = !this.awaitCandidates(request.workflowTimeout(), candidateIds);
    if (timedOut) {
      this.activities.abortSampleWalletCreation(walletId);
      throw ApplicationFailure.newFailure("Sample wallet creation timed out", "SAMPLE_WALLET_TIMEOUT");
    }
    return this.activities.finishSampleWalletCreation(walletId, investmentIds, candidateIds);
  }

  private boolean awaitCandidates(Duration workflowTimeout, java.util.List<String> candidateIds) {
    return Workflow.await(
      workflowTimeout,
      () -> candidateIds.stream().allMatch(candidateId -> Boolean.TRUE.equals(this.processedCandidates.get(candidateId)))
    );
  }

  @Override
  public void candidateProcessed(String candidateId, CandidateStatus status, boolean processed) {
    this.candidateStatuses.put(candidateId, status);
    this.processedCandidates.put(candidateId, processed);
  }

  @Override
  public Map<String, CandidateStatus> candidateStatuses() {
    return Map.copyOf(this.candidateStatuses);
  }
}
