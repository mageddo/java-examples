package com.mageddo.temporal.sample_wallet;

import io.quarkus.arc.properties.IfBuildProperty;
import io.smallrye.config.ConfigMapping;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import lombok.Getter;

@Getter
@Singleton
@IfBuildProperty(name = "sample-wallet.temporal.enabled", stringValue = "true")
public class TemporalClientProvider {

  final WorkflowServiceStubs workflowServiceStubs;
  final WorkflowClient workflowClient;
  final TemporalRuntimeConfig config;

  public TemporalClientProvider(TemporalRuntimeConfig config) {
    this.config = config;
    this.workflowServiceStubs = WorkflowServiceStubs.newServiceStubs(
      WorkflowServiceStubsOptions.newBuilder()
        .setTarget(config.target())
        .build()
    );
    this.workflowClient = WorkflowClient.newInstance(
      this.workflowServiceStubs,
      WorkflowClientOptions.newBuilder()
        .setNamespace(config.namespace())
        .build()
    );
  }

  @PreDestroy
  void close() {
    this.workflowServiceStubs.shutdown();
  }

  @ConfigMapping(prefix = "sample-wallet.temporal")
  public interface TemporalRuntimeConfig {

    boolean enabled();

    String namespace();

    String target();

    String taskQueue();

    java.time.Duration workflowTimeout();

    Background background();

    interface Background {

      java.time.Duration investmentCreationDelay();

      java.time.Duration candidateProcessingDelay();

      int maxCandidateAttempts();
    }
  }
}
