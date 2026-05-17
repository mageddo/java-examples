package com.mageddo.temporal.samplewallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mageddo.temporal.samplewallet.activity.SampleWalletCreationActivitiesImpl;
import com.mageddo.temporal.samplewallet.dataprovider.FinancialEventCandidateRepo;
import com.mageddo.temporal.samplewallet.dataprovider.HibernateSessionFactoryProvider;
import com.mageddo.temporal.samplewallet.dataprovider.InvestmentRepo;
import com.mageddo.temporal.samplewallet.dataprovider.InvestorRepo;
import com.mageddo.temporal.samplewallet.dataprovider.WalletRepo;
import com.mageddo.temporal.samplewallet.domain.WalletStatus;
import com.mageddo.temporal.samplewallet.domain.templates.InvestorTemplates;
import com.mageddo.temporal.samplewallet.domain.templates.SampleWalletCreationRequestTemplates;
import com.mageddo.temporal.samplewallet.workflow.SampleWalletCreationWorkflow;
import com.mageddo.temporal.samplewallet.workflow.SampleWalletCreationWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestEnvironmentOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SampleWalletCreationWorkflowTest {

  static final String TASK_QUEUE = "sample-wallet-task-queue";

  EmbeddedPostgres embeddedPostgres;
  HibernateSessionFactoryProvider sessionFactoryProvider;
  SessionFactory sessionFactory;
  InvestorRepo investorRepo;
  WalletRepo walletRepo;
  InvestmentRepo investmentRepo;
  FinancialEventCandidateRepo candidateRepo;
  TestWorkflowEnvironment testWorkflowEnvironment;
  Worker worker;
  java.util.concurrent.ExecutorService backgroundExecutor;

  @BeforeEach
  void setUp() throws IOException, SQLException {
    this.embeddedPostgres = EmbeddedPostgres.start();
    this.initializeSchema();
    this.sessionFactoryProvider = new HibernateSessionFactoryProvider(Map.of(
      "hibernate.connection.url", this.embeddedPostgres.getJdbcUrl("postgres", "postgres"),
      "hibernate.connection.username", "postgres",
      "hibernate.connection.password", "postgres",
      "hibernate.connection.driver_class", "org.postgresql.Driver",
      "hibernate.show_sql", false,
      "hibernate.format_sql", true,
      "hibernate.hbm2ddl.auto", "none"
    ));
    this.sessionFactory = this.sessionFactoryProvider.get();
    this.investorRepo = new InvestorRepo(this.sessionFactory);
    this.walletRepo = new WalletRepo(this.sessionFactory);
    this.investmentRepo = new InvestmentRepo(this.sessionFactory);
    this.candidateRepo = new FinancialEventCandidateRepo(this.sessionFactory);
    this.backgroundExecutor = Executors.newCachedThreadPool();
  }

  @AfterEach
  void tearDown() throws IOException {
    if (this.testWorkflowEnvironment != null) {
      this.testWorkflowEnvironment.close();
    }
    if (this.backgroundExecutor != null) {
      this.backgroundExecutor.shutdownNow();
    }
    if (this.sessionFactoryProvider != null) {
      this.sessionFactoryProvider.close();
    }
    if (this.embeddedPostgres != null) {
      this.embeddedPostgres.close();
    }
  }

  @Test
  void shouldCreateReadyWalletAndProcessCandidates() {
    var investor = InvestorTemplates.moderado("investor-success");
    this.investorRepo.save(investor);
    this.startTemporalEnvironment(Set.of(), Set.of(), 100);

    var workflow = this.newWorkflow("investor-success-workflow");

    var result = workflow.create(SampleWalletCreationRequestTemplates.defaultRequest(investor.getId()));

    var wallet = this.walletRepo.findById(result.walletId());
    var investments = this.investmentRepo.findByWalletId(result.walletId());
    var candidates = this.candidateRepo.findByWalletId(result.walletId());

    assertThat(wallet.getStatus()).isEqualTo(WalletStatus.READY);
    assertThat(investments).hasSize(3).allMatch(com.mageddo.temporal.samplewallet.domain.Investment::isCreated);
    assertThat(candidates).hasSize(3).allMatch(com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate::isProcessed);
  }

  @Test
  void shouldRetryCandidateProcessingAndStillFinishWallet() {
    var investor = InvestorTemplates.arrojado("investor-retry");
    this.investorRepo.save(investor);
    this.startTemporalEnvironment(Set.of("candidate-1"), Set.of(), 50);

    var workflow = this.newWorkflow("investor-retry-workflow");

    var result = workflow.create(SampleWalletCreationRequestTemplates.defaultRequest(investor.getId()));

    var candidates = this.candidateRepo.findByWalletId(result.walletId());

    assertThat(candidates).hasSize(3);
    assertThat(candidates)
      .filteredOn(candidate -> candidate.getAttempts() > 1)
      .hasSize(1);
    assertThat(this.walletRepo.findById(result.walletId()).getStatus()).isEqualTo(WalletStatus.READY);
  }

  @Test
  void shouldAbortWalletWhenCandidateProcessingTimesOut() {
    var investor = InvestorTemplates.moderado("investor-timeout");
    this.investorRepo.save(investor);
    this.startTemporalEnvironment(Set.of(), Set.of("candidate-1"), 1_000);

    var workflow = this.newWorkflow("investor-timeout-workflow");

    assertThatThrownBy(() -> workflow.create(SampleWalletCreationRequestTemplates.timeoutRequest(investor.getId())))
      .cause()
      .isInstanceOf(ApplicationFailure.class)
      .hasMessageContaining("SAMPLE_WALLET_TIMEOUT");

    var wallet = this.walletRepo.findByInvestorId(investor.getId()).getFirst();

    assertThat(wallet.getStatus()).isEqualTo(WalletStatus.ABORTED);
  }

  private void startTemporalEnvironment(Set<String> failFirstAttemptCandidateIds, Set<String> alwaysSlowCandidateIds, long candidateDelayMillis) {
    this.testWorkflowEnvironment = TestWorkflowEnvironment.newInstance(
      TestEnvironmentOptions.newBuilder()
        .setUseTimeskipping(false)
        .build()
    );
    var workflowClient = this.testWorkflowEnvironment.getWorkflowClient();
    var dispatcher = new SampleWalletCreationActivitiesImpl.BackgroundJobDispatcher(
      this.investmentRepo,
      this.candidateRepo,
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
      this.investorRepo,
      this.walletRepo,
      this.investmentRepo,
      this.candidateRepo,
      workflowClient,
      dispatcher
    );
    this.worker = this.testWorkflowEnvironment.newWorker(TASK_QUEUE);
    this.worker.registerWorkflowImplementationTypes(SampleWalletCreationWorkflowImpl.class);
    this.worker.registerActivitiesImplementations(activities);
    this.testWorkflowEnvironment.start();
  }

  private SampleWalletCreationWorkflow newWorkflow(String workflowId) {
    return this.testWorkflowEnvironment.getWorkflowClient().newWorkflowStub(
      SampleWalletCreationWorkflow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue(TASK_QUEUE)
        .setWorkflowId(workflowId)
        .setWorkflowExecutionTimeout(Duration.ofMinutes(2))
        .build()
    );
  }

  private void initializeSchema() throws SQLException {
    try (var connection = this.embeddedPostgres.getPostgresDatabase().getConnection();
         var statement = connection.createStatement()) {
      statement.execute("""
        create table investor (
          id varchar(100) primary key,
          profile varchar(30) not null
        )
        """);
      statement.execute("""
        create table wallet (
          id varchar(100) primary key,
          investor_id varchar(100) not null,
          status varchar(30) not null,
          created_at timestamp with time zone not null,
          ready_at timestamp with time zone,
          aborted_at timestamp with time zone
        )
        """);
      statement.execute("""
        create table investment (
          id varchar(100) primary key,
          wallet_id varchar(100) not null,
          investor_id varchar(100) not null,
          base_investment_id varchar(100) not null,
          profile varchar(30) not null,
          created boolean not null
        )
        """);
      statement.execute("""
        create table financial_event_candidate (
          id varchar(100) primary key,
          investment_id varchar(100) not null,
          status varchar(30) not null,
          processed boolean not null,
          attempts integer not null,
          processed_at timestamp with time zone
        )
        """);
    }
  }
}
