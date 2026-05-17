package com.mageddo.temporal.samplewallet.workflow;

import com.mageddo.temporal.samplewallet.domain.CandidateStatus;
import com.mageddo.temporal.samplewallet.domain.SampleWalletCreationRequest;
import com.mageddo.temporal.samplewallet.domain.SampleWalletCreationResult;
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
