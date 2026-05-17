package com.mageddo.temporal.sample_wallet;

import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationRequest;
import com.mageddo.temporal.sample_wallet.workflow.SampleWalletCreationWorkflow;
import io.quarkus.arc.properties.IfBuildProperty;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Singleton
@IfBuildProperty(name = "sample-wallet.temporal.enabled", stringValue = "true")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SampleWalletWorkflowService {

  final TemporalClientProvider temporalClientProvider;

  public WorkflowExecution start(String investorId, Duration workflowTimeout) {
    final var workflow = this.temporalClientProvider.getWorkflowClient().newWorkflowStub(
      SampleWalletCreationWorkflow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue(this.temporalClientProvider.getConfig().taskQueue())
        .setWorkflowId(this.newWorkflowId(investorId))
        .setWorkflowExecutionTimeout(this.temporalClientProvider.getConfig().workflowTimeout())
        .build()
    );
    return WorkflowClient.start(
      workflow::create,
      SampleWalletCreationRequest.builder()
        .investorId(investorId)
        .workflowTimeout(workflowTimeout)
        .build()
    );
  }

  private String newWorkflowId(String investorId) {
    return "sample-wallet-" + investorId + "-" + UUID.randomUUID();
  }
}
