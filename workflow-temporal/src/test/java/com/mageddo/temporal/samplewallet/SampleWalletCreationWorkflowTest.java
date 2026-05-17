package com.mageddo.temporal.samplewallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mageddo.investment_product.investment.Investment;
import com.mageddo.temporal.samplewallet.activity.SampleWalletCreationActivitiesImpl;
import com.mageddo.investment_product.financial_event_candidate.dataprovider.FinancialEventCandidateDAO;
import com.mageddo.investment_product.investment.dataprovider.InvestmentDAO;
import com.mageddo.temporal.samplewallet.dataprovider.InvestorDAO;
import com.mageddo.temporal.samplewallet.dataprovider.WalletDAO;
import com.mageddo.temporal.samplewallet.domain.WalletStatus;
import com.mageddo.temporal.samplewallet.domain.templates.InvestorTemplates;
import com.mageddo.temporal.samplewallet.domain.templates.SampleWalletCreationRequestTemplates;
import com.mageddo.temporal.samplewallet.workflow.SampleWalletCreationWorkflow;
import com.mageddo.temporal.samplewallet.workflow.SampleWalletCreationWorkflowImpl;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestEnvironmentOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(EmbeddedPostgresQuarkusTestResource.class)
class SampleWalletCreationWorkflowTest {

  static final String TASK_QUEUE = "sample-wallet-task-queue";

  @Inject
  InvestorDAO investorDAO;

  @Inject
  WalletDAO walletDAO;

  @Inject
  InvestmentDAO investmentDAO;

  @Inject
  FinancialEventCandidateDAO candidateDAO;

  @Inject
  TestDatabaseControl testDatabaseControl;

  TestWorkflowEnvironment testWorkflowEnvironment;
  Worker worker;
  ExecutorService backgroundExecutor;

  @BeforeEach
  void setUp() {
    this.testDatabaseControl.clear();
    this.backgroundExecutor = Executors.newCachedThreadPool();
  }

  @AfterEach
  void tearDown() {
    if (this.testWorkflowEnvironment != null) {
      this.testWorkflowEnvironment.close();
    }
    if (this.backgroundExecutor != null) {
      this.backgroundExecutor.shutdownNow();
    }
  }

  @Test
  void shouldCreateReadyWalletAndProcessCandidates() {
    var investor = InvestorTemplates.moderado("investor-success");
    this.investorDAO.save(investor);
    this.startTemporalEnvironment(Set.of(), Set.of(), 100);

    var workflow = this.newWorkflow("investor-success-workflow");

    var result = workflow.create(SampleWalletCreationRequestTemplates.defaultRequest(investor.getId()));

    var wallet = this.walletDAO.findById(result.walletId());
    var investments = this.investmentDAO.findByWalletId(result.walletId());
    var candidates = this.candidateDAO.findByWalletId(result.walletId());

    assertThat(wallet.getStatus()).isEqualTo(WalletStatus.READY);
    assertThat(investments).hasSize(3).allMatch(Investment::isCreated);
    assertThat(candidates).hasSize(3).allMatch(com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate::isProcessed);
  }

  @Test
  void shouldRetryCandidateProcessingAndStillFinishWallet() {
    var investor = InvestorTemplates.arrojado("investor-retry");
    this.investorDAO.save(investor);
    this.startTemporalEnvironment(Set.of("candidate-1"), Set.of(), 50);

    var workflow = this.newWorkflow("investor-retry-workflow");

    var result = workflow.create(SampleWalletCreationRequestTemplates.defaultRequest(investor.getId()));

    var candidates = this.candidateDAO.findByWalletId(result.walletId());

    assertThat(candidates).hasSize(3);
    assertThat(candidates)
      .filteredOn(candidate -> candidate.getAttempts() > 1)
      .hasSize(1);
    assertThat(this.walletDAO.findById(result.walletId()).getStatus()).isEqualTo(WalletStatus.READY);
  }

  @Test
  void shouldAbortWalletWhenCandidateProcessingTimesOut() {
    var investor = InvestorTemplates.moderado("investor-timeout");
    this.investorDAO.save(investor);
    this.startTemporalEnvironment(Set.of(), Set.of("candidate-1"), 1_000);

    var workflow = this.newWorkflow("investor-timeout-workflow");

    assertThatThrownBy(() -> workflow.create(SampleWalletCreationRequestTemplates.timeoutRequest(investor.getId())))
      .cause()
      .isInstanceOf(ApplicationFailure.class)
      .hasMessageContaining("SAMPLE_WALLET_TIMEOUT");

    var wallet = this.walletDAO.findByInvestorId(investor.getId()).getFirst();

    assertThat(wallet.getStatus()).isEqualTo(WalletStatus.ABORTED);
  }

  void startTemporalEnvironment(Set<String> failFirstAttemptCandidateIds, Set<String> alwaysSlowCandidateIds, long candidateDelayMillis) {
    this.testWorkflowEnvironment = TestWorkflowEnvironment.newInstance(
      TestEnvironmentOptions.newBuilder()
        .setUseTimeskipping(false)
        .build()
    );
    var workflowClient = this.testWorkflowEnvironment.getWorkflowClient();
    var dispatcher = new SampleWalletCreationActivitiesImpl.BackgroundJobDispatcher(
      this.investmentDAO,
      this.candidateDAO,
      workflowClient,
      this.backgroundExecutor,
      new SampleWalletCreationActivitiesImpl.BackgroundJobPolicy(
        25,
        candidateDelayMillis,
        failFirstAttemptCandidateIds,
        alwaysSlowCandidateIds,
        2
      )
    );
    var activities = new SampleWalletCreationActivitiesImpl(
      this.investorDAO,
      this.walletDAO,
      this.investmentDAO,
      this.candidateDAO,
      workflowClient,
      dispatcher
    );
    this.worker = this.testWorkflowEnvironment.newWorker(TASK_QUEUE);
    this.worker.registerWorkflowImplementationTypes(SampleWalletCreationWorkflowImpl.class);
    this.worker.registerActivitiesImplementations(activities);
    this.testWorkflowEnvironment.start();
  }

  SampleWalletCreationWorkflow newWorkflow(String workflowId) {
    return this.testWorkflowEnvironment.getWorkflowClient().newWorkflowStub(
      SampleWalletCreationWorkflow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue(TASK_QUEUE)
        .setWorkflowId(workflowId)
        .setWorkflowExecutionTimeout(Duration.ofMinutes(2))
        .build()
    );
  }
}
