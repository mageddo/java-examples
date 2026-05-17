package com.mageddo.temporal.samplewallet.activity;

import com.mageddo.temporal.samplewallet.dataprovider.FinancialEventCandidateRepo;
import com.mageddo.temporal.samplewallet.dataprovider.InvestmentRepo;
import com.mageddo.temporal.samplewallet.dataprovider.InvestorRepo;
import com.mageddo.temporal.samplewallet.dataprovider.WalletRepo;
import com.mageddo.temporal.samplewallet.domain.CandidateStatus;
import com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate;
import com.mageddo.temporal.samplewallet.domain.Investment;
import com.mageddo.temporal.samplewallet.domain.Investor;
import com.mageddo.temporal.samplewallet.domain.InvestorProfile;
import com.mageddo.temporal.samplewallet.domain.SampleWalletCreationResult;
import com.mageddo.temporal.samplewallet.domain.Wallet;
import com.mageddo.temporal.samplewallet.domain.WalletStatus;
import com.mageddo.temporal.samplewallet.workflow.SampleWalletCreationWorkflow;
import io.temporal.client.WorkflowClient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SampleWalletCreationActivitiesImpl implements SampleWalletCreationActivities {

  private static final Map<InvestorProfile, List<String>> BASE_INVESTMENTS_BY_PROFILE = Map.of(
    InvestorProfile.ARROJADO, List.of("ETF-USA", "STOCK-TECH", "CRYPTO-BTC"),
    InvestorProfile.MODERADO, List.of("ETF-GLOBAL", "BOND-IPCA", "REIT-US"),
    InvestorProfile.CONSERVADOR, List.of("TREASURY-SELIC", "CDB-DI", "LCI-2027")
  );

  private final InvestorRepo investorRepo;
  private final WalletRepo walletRepo;
  private final InvestmentRepo investmentRepo;
  private final FinancialEventCandidateRepo candidateRepo;
  private final WorkflowClient workflowClient;
  private final BackgroundJobDispatcher backgroundJobDispatcher;
  private final AtomicInteger walletSequence = new AtomicInteger();
  private final AtomicInteger investmentSequence = new AtomicInteger();
  private final AtomicInteger candidateSequence = new AtomicInteger();

  public SampleWalletCreationActivitiesImpl(
    InvestorRepo investorRepo,
    WalletRepo walletRepo,
    InvestmentRepo investmentRepo,
    FinancialEventCandidateRepo candidateRepo,
    WorkflowClient workflowClient,
    BackgroundJobDispatcher backgroundJobDispatcher
  ) {
    this.investorRepo = investorRepo;
    this.walletRepo = walletRepo;
    this.investmentRepo = investmentRepo;
    this.candidateRepo = candidateRepo;
    this.workflowClient = workflowClient;
    this.backgroundJobDispatcher = backgroundJobDispatcher;
  }

  @Override
  public String createWallet(String investorId) {
    var walletId = "wallet-" + this.walletSequence.incrementAndGet();
    this.walletRepo.save(Wallet.builder()
      .id(walletId)
      .investorId(investorId)
      .status(WalletStatus.CREATING)
      .createdAt(Instant.now())
      .build());
    return walletId;
  }

  @Override
  public List<String> createInvestments(String investorId, String walletId) {
    var investor = this.requireInvestor(investorId);
    var baseInvestmentIds = BASE_INVESTMENTS_BY_PROFILE.get(investor.getProfile());
    var investmentIds = baseInvestmentIds.stream()
      .map(baseInvestmentId -> this.createInvestment(walletId, investor, baseInvestmentId))
      .toList();
    investmentIds.forEach(this.backgroundJobDispatcher::scheduleInvestmentCreation);
    return investmentIds.stream()
      .map(Investment::getId)
      .toList();
  }

  @Override
  public List<String> createFinancialEventCandidates(String workflowId, String walletId, List<String> investmentIds) {
    var candidateIds = investmentIds.stream()
      .map(this::createCandidate)
      .toList();
    candidateIds.forEach(candidateId -> this.backgroundJobDispatcher.scheduleCandidateProcessing(workflowId, walletId, candidateId));
    return candidateIds;
  }

  @Override
  public Map<String, Boolean> fanOutCandidates(List<String> candidateIds) {
    var fanOut = new LinkedHashMap<String, Boolean>();
    for (var candidateId : candidateIds) {
      fanOut.put(candidateId, Boolean.TRUE);
    }
    return fanOut;
  }

  @Override
  public SampleWalletCreationResult finishSampleWalletCreation(String walletId, List<String> investmentIds, List<String> candidateIds) {
    var wallet = this.walletRepo.findById(walletId);
    this.walletRepo.save(wallet.toBuilder()
      .status(WalletStatus.READY)
      .readyAt(Instant.now())
      .build());
    return SampleWalletCreationResult.builder()
      .walletId(walletId)
      .investmentIds(investmentIds)
      .candidateIds(candidateIds)
      .build();
  }

  @Override
  public void abortSampleWalletCreation(String walletId) {
    var wallet = this.walletRepo.findById(walletId);
    if (wallet == null || wallet.getStatus() == WalletStatus.READY) {
      return;
    }
    this.walletRepo.save(wallet.toBuilder()
      .status(WalletStatus.ABORTED)
      .abortedAt(Instant.now())
      .build());
  }

  private Investor requireInvestor(String investorId) {
    var investor = this.investorRepo.findById(investorId);
    if (investor == null) {
      throw new IllegalArgumentException("Investor not found: " + investorId);
    }
    return investor;
  }

  private Investment createInvestment(String walletId, Investor investor, String baseInvestmentId) {
    var investment = Investment.builder()
      .id("investment-" + this.investmentSequence.incrementAndGet())
      .walletId(walletId)
      .investorId(investor.getId())
      .baseInvestmentId(baseInvestmentId)
      .profile(investor.getProfile())
      .created(false)
      .build();
    this.investmentRepo.save(investment);
    return investment;
  }

  private String createCandidate(String investmentId) {
    var candidate = FinancialEventCandidate.builder()
      .id("candidate-" + this.candidateSequence.incrementAndGet())
      .investmentId(investmentId)
      .status(CandidateStatus.PENDING)
      .processed(false)
      .attempts(0)
      .build();
    this.candidateRepo.save(candidate);
    return candidate.getId();
  }

  public static class BackgroundJobDispatcher {

    private final InvestmentRepo investmentRepo;
    private final FinancialEventCandidateRepo candidateRepo;
    private final WorkflowClient workflowClient;
    private final ExecutorService executorService;
    private final BackgroundJobPolicy policy;
    private final Set<String> failedOnceCandidateIds = ConcurrentHashMap.newKeySet();

    public BackgroundJobDispatcher(
      InvestmentRepo investmentRepo,
      FinancialEventCandidateRepo candidateRepo,
      WorkflowClient workflowClient,
      ExecutorService executorService,
      BackgroundJobPolicy policy
    ) {
      this.investmentRepo = investmentRepo;
      this.candidateRepo = candidateRepo;
      this.workflowClient = workflowClient;
      this.executorService = executorService;
      this.policy = policy;
    }

    public void scheduleInvestmentCreation(Investment investment) {
      this.executorService.submit(() -> {
        this.sleep(this.policy.investmentCreationDelayMillis());
        var persisted = this.investmentRepo.findById(investment.getId());
        this.investmentRepo.save(persisted.toBuilder()
          .created(true)
          .build());
      });
    }

    public void scheduleCandidateProcessing(String workflowId, String walletId, String candidateId) {
      this.executorService.submit(() -> this.processCandidate(workflowId, walletId, candidateId));
    }

    private void processCandidate(String workflowId, String walletId, String candidateId) {
      for (var attempts = 1; attempts <= this.policy.maxCandidateAttempts(); attempts++) {
        this.waitUntilInvestmentIsCreated(candidateId);
        this.sleep(this.policy.candidateProcessingDelayMillis());
        if (this.policy.alwaysSlowCandidateIds().contains(candidateId)) {
          continue;
        }
        if (this.policy.failFirstAttemptCandidateIds().contains(candidateId)
            && this.failedOnceCandidateIds.add(candidateId)) {
          var candidate = this.candidateRepo.findById(candidateId);
          this.candidateRepo.save(candidate.toBuilder()
            .attempts(attempts)
            .build());
          continue;
        }
        var candidate = this.candidateRepo.findById(candidateId);
        var processedCandidate = candidate.toBuilder()
          .status(CandidateStatus.MATCHED)
          .processed(true)
          .processedAt(Instant.now())
          .attempts(attempts)
          .build();
        this.candidateRepo.save(processedCandidate);
        this.signalProcessed(workflowId, processedCandidate);
        return;
      }
      var candidate = this.candidateRepo.findById(candidateId);
      var rejectedCandidate = candidate.toBuilder()
        .status(CandidateStatus.REJECTED)
        .processed(true)
        .processedAt(Instant.now())
        .attempts(this.policy.maxCandidateAttempts())
        .build();
      this.candidateRepo.save(rejectedCandidate);
      this.signalProcessed(workflowId, rejectedCandidate);
    }

    private void signalProcessed(String workflowId, FinancialEventCandidate candidate) {
      var workflow = this.workflowClient.newWorkflowStub(SampleWalletCreationWorkflow.class, workflowId);
      workflow.candidateProcessed(candidate.getId(), candidate.getStatus(), candidate.isProcessed());
    }

    private void waitUntilInvestmentIsCreated(String candidateId) {
      while (true) {
        var candidate = this.candidateRepo.findById(candidateId);
        var investment = this.investmentRepo.findById(candidate.getInvestmentId());
        if (investment.isCreated()) {
          return;
        }
        this.sleep(10);
      }
    }

    private void sleep(long delayMillis) {
      try {
        TimeUnit.MILLISECONDS.sleep(delayMillis);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    }
  }

    public record BackgroundJobPolicy(
      long investmentCreationDelayMillis,
      long candidateProcessingDelayMillis,
      Set<String> failFirstAttemptCandidateIds,
      Set<String> alwaysSlowCandidateIds,
      int maxCandidateAttempts
    ) {
    }
}
