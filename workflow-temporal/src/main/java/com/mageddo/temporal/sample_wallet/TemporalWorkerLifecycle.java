package com.mageddo.temporal.sample_wallet;

import com.mageddo.investment_product.financial_event_candidate.FinancialEventCandidateService;
import com.mageddo.investment_product.investment.InvestmentService;
import com.mageddo.investment_product.investor.InvestorService;
import com.mageddo.investment_product.wallet.WalletService;
import com.mageddo.temporal.sample_wallet.activity.SampleWalletCreationActivitiesImpl;
import com.mageddo.temporal.sample_wallet.workflow.SampleWalletCreationWorkflowImpl;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.temporal.worker.WorkerFactory;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;

@Singleton
@IfBuildProperty(name = "sample-wallet.temporal.enabled", stringValue = "true")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TemporalWorkerLifecycle {

  final TemporalClientProvider temporalClientProvider;
  final InvestorService investorService;
  final WalletService walletService;
  final InvestmentService investmentService;
  final FinancialEventCandidateService financialEventCandidateService;

  WorkerFactory workerFactory;
  ExecutorService backgroundExecutor;

  void onStart(@Observes StartupEvent startupEvent) {
    final var config = this.temporalClientProvider.getConfig();
    this.backgroundExecutor = Executors.newCachedThreadPool();
    final var dispatcher = new SampleWalletCreationActivitiesImpl.BackgroundJobDispatcher(
      this.investmentService,
      this.financialEventCandidateService,
      this.temporalClientProvider.getWorkflowClient(),
      this.backgroundExecutor,
      new SampleWalletCreationActivitiesImpl.BackgroundJobPolicy(
        config.background().investmentCreationDelay().toMillis(),
        config.background().candidateProcessingDelay().toMillis(),
        Set.of(),
        Set.of(),
        config.background().maxCandidateAttempts()
      )
    );
    final var activities = new SampleWalletCreationActivitiesImpl(
      this.investorService,
      this.walletService,
      this.investmentService,
      this.financialEventCandidateService,
      this.temporalClientProvider.getWorkflowClient(),
      dispatcher
    );
    this.workerFactory = WorkerFactory.newInstance(this.temporalClientProvider.getWorkflowClient());
    final var worker = this.workerFactory.newWorker(config.taskQueue());
    worker.registerWorkflowImplementationTypes(SampleWalletCreationWorkflowImpl.class);
    worker.registerActivitiesImplementations(activities);
    this.workerFactory.start();
  }

  void onStop(@Observes ShutdownEvent shutdownEvent) {
    if (this.workerFactory != null) {
      this.workerFactory.shutdown();
    }
    if (this.backgroundExecutor != null) {
      this.backgroundExecutor.shutdownNow();
    }
  }
}
