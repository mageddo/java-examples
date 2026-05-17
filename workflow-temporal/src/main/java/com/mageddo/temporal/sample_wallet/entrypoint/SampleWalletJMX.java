package com.mageddo.temporal.sample_wallet.entrypoint;

import com.mageddo.temporal.sample_wallet.SampleWalletWorkflowService;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.time.Duration;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/jmx/sample-wallets")
@Tag(name = "sample-wallet-jmx")
@Singleton
@IfBuildProperty(name = "sample-wallet.temporal.enabled", stringValue = "true")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SampleWalletJMX {

  final SampleWalletWorkflowService sampleWalletWorkflowService;

  @POST
  @Path("/{investorId}")
  public StartSampleWalletWorkflowRes createSampleWallet(
    @PathParam("investorId") String investorId,
    @DefaultValue("120") @QueryParam("timeoutSeconds") long timeoutSeconds
  ) {
    final var workflowExecution = this.sampleWalletWorkflowService.start(investorId, Duration.ofSeconds(timeoutSeconds));
    return StartSampleWalletWorkflowRes.builder()
      .workflowId(workflowExecution.getWorkflowId())
      .runId(workflowExecution.getRunId())
      .build();
  }

  @Builder
  public record StartSampleWalletWorkflowRes(
    String workflowId,
    String runId
  ) {
  }
}
