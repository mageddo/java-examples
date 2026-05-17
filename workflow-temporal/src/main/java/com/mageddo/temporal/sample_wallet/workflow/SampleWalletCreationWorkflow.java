package com.mageddo.temporal.sample_wallet.workflow;

import com.mageddo.investment_product.financial_event_candidate.CandidateStatus;
import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationRequest;
import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationResult;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.Map;

@WorkflowInterface
public interface SampleWalletCreationWorkflow {

  @WorkflowMethod
  SampleWalletCreationResult create(SampleWalletCreationRequest request);

  @SignalMethod
  void candidateProcessed(String candidateId, CandidateStatus status, boolean processed);

  @QueryMethod
  Map<String, CandidateStatus> candidateStatuses();
}
