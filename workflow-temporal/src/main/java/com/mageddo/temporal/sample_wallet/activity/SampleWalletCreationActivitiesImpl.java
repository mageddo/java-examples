package com.mageddo.temporal.sample_wallet.activity;

import com.mageddo.investment_product.financial_event_candidate.CandidateStatus;
import com.mageddo.investment_product.financial_event_candidate.FinancialEventCandidate;
import com.mageddo.investment_product.financial_event_candidate.FinancialEventCandidateService;
import com.mageddo.investment_product.investment.Investment;
import com.mageddo.investment_product.investment.InvestmentService;
import com.mageddo.investment_product.investor.Investor;
import com.mageddo.investment_product.investor.InvestorService;
import com.mageddo.investment_product.wallet.Wallet;
import com.mageddo.investment_product.wallet.WalletService;
import com.mageddo.investment_product.investor.InvestorProfile;
import com.mageddo.temporal.sample_wallet.domain.SampleWalletCreationResult;
import com.mageddo.investment_product.wallet.WalletStatus;
import com.mageddo.temporal.sample_wallet.workflow.SampleWalletCreationWorkflow;
import io.temporal.client.WorkflowClient;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SampleWalletCreationActivitiesImpl implements SampleWalletCreationActivities {

  private static final Map<InvestorProfile, List<String>> BASE_INVESTMENTS_BY_PROFILE = Map.of(
    InvestorProfile.ARROJADO, List.of("ETF-USA", "STOCK-TECH", "CRYPTO-BTC"),
    InvestorProfile.MODERADO, List.of("ETF-GLOBAL", "BOND-IPCA", "REIT-US"),
    InvestorProfile.CONSERVADOR, List.of("TREASURY-SELIC", "CDB-DI", "LCI-2027")
  );

  private final InvestorService investorService;
  private final WalletService walletService;
  private final InvestmentService investmentService;
  private final FinancialEventCandidateService financialEventCandidateService;
  private final WorkflowClient workflowClient;
  private final BackgroundJobDispatcher backgroundJobDispatcher;
  private final AtomicInteger walletSequence = new AtomicInteger();
  private final AtomicInteger investmentSequence = new AtomicInteger();
  private final AtomicInteger candidateSequence = new AtomicInteger();

  @Override
  public String createWallet(String investorId) {
    final var walletId = "wallet-" + this.walletSequence.incrementAndGet();
    this.walletService.save(Wallet.builder()
      .id(walletId)
      .investorId(investorId)
      .status(WalletStatus.CREATING)
      .createdAt(Instant.now())
      .build());
    return walletId;
  }

  @Override
  public List<String> createInvestments(String investorId, String walletId) {
    final var investor = this.requireInvestor(investorId);
    final var baseInvestmentIds = BASE_INVESTMENTS_BY_PROFILE.get(investor.getProfile());
    final var investmentIds = baseInvestmentIds.stream()
      .map(baseInvestmentId -> this.createInvestment(walletId, investor, baseInvestmentId))
      .toList();
    investmentIds.forEach(this.backgroundJobDispatcher::scheduleInvestmentCreation);
    return investmentIds.stream()
      .map(Investment::getId)
      .toList();
  }

  @Override
  public List<String> createFinancialEventCandidates(String workflowId, String walletId, List<String> investmentIds) {
    final var candidateIds = investmentIds.stream()
      .map(this::createCandidate)
      .toList();
    candidateIds.forEach(candidateId -> this.backgroundJobDispatcher.scheduleCandidateProcessing(workflowId, walletId, candidateId));
    return candidateIds;
  }

  @Override
  public Map<String, Boolean> fanOutCandidates(List<String> candidateIds) {
    final var fanOut = new LinkedHashMap<String, Boolean>();
    for (final var candidateId : candidateIds) {
      fanOut.put(candidateId, Boolean.TRUE);
    }
    return fanOut;
  }

  @Override
  public SampleWalletCreationResult finishSampleWalletCreation(String walletId, List<String> investmentIds, List<String> candidateIds) {
    final var wallet = this.walletService.findById(walletId);
    this.walletService.save(wallet.toBuilder()
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
    final var wallet = this.walletService.findById(walletId);
    if (wallet == null || wallet.getStatus() == WalletStatus.READY) {
      return;
    }
    this.walletService.save(wallet.toBuilder()
      .status(WalletStatus.ABORTED)
      .abortedAt(Instant.now())
      .build());
  }

  private Investor requireInvestor(String investorId) {
    final var investor = this.investorService.findById(investorId);
    if (investor == null) {
      throw new IllegalArgumentException("Investor not found: " + investorId);
    }
    return investor;
  }

  private Investment createInvestment(String walletId, Investor investor, String baseInvestmentId) {
    final var investment = Investment.builder()
      .id("investment-" + this.investmentSequence.incrementAndGet())
      .walletId(walletId)
      .investorId(investor.getId())
      .baseInvestmentId(baseInvestmentId)
      .profile(investor.getProfile())
      .created(false)
      .build();
    this.investmentService.save(investment);
    return investment;
  }

  private String createCandidate(String investmentId) {
    final var candidate = FinancialEventCandidate.builder()
      .id("candidate-" + this.candidateSequence.incrementAndGet())
      .investmentId(investmentId)
      .status(CandidateStatus.PENDING)
      .processed(false)
      .attempts(0)
      .build();
    this.financialEventCandidateService.save(candidate);
    return candidate.getId();
  }

  @RequiredArgsConstructor
  public static class BackgroundJobDispatcher {

    private final InvestmentService investmentService;
    private final FinancialEventCandidateService financialEventCandidateService;
    private final WorkflowClient workflowClient;
    private final ExecutorService executorService;
    private final BackgroundJobPolicy policy;
    private final Set<String> failedOnceCandidateIds = ConcurrentHashMap.newKeySet();

    public void scheduleInvestmentCreation(Investment investment) {
      this.executorService.submit(() -> {
        this.sleep(this.policy.investmentCreationDelayMillis());
        final var persisted = this.investmentService.findById(investment.getId());
        this.investmentService.save(persisted.toBuilder()
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
          final var candidate = this.financialEventCandidateService.findById(candidateId);
          this.financialEventCandidateService.save(candidate.toBuilder()
            .attempts(attempts)
            .build());
          continue;
        }
        final var candidate = this.financialEventCandidateService.findById(candidateId);
        final var processedCandidate = candidate.toBuilder()
          .status(CandidateStatus.MATCHED)
          .processed(true)
          .processedAt(Instant.now())
          .attempts(attempts)
          .build();
        this.financialEventCandidateService.save(processedCandidate);
        this.signalProcessed(workflowId, processedCandidate);
        return;
      }
      final var candidate = this.financialEventCandidateService.findById(candidateId);
      final var rejectedCandidate = candidate.toBuilder()
        .status(CandidateStatus.REJECTED)
        .processed(true)
        .processedAt(Instant.now())
        .attempts(this.policy.maxCandidateAttempts())
        .build();
      this.financialEventCandidateService.save(rejectedCandidate);
      this.signalProcessed(workflowId, rejectedCandidate);
    }

    private void signalProcessed(String workflowId, FinancialEventCandidate candidate) {
      final var workflow = this.workflowClient.newWorkflowStub(SampleWalletCreationWorkflow.class, workflowId);
      workflow.candidateProcessed(candidate.getId(), candidate.getStatus(), candidate.isProcessed());
    }

    private void waitUntilInvestmentIsCreated(String candidateId) {
      while (true) {
        final var candidate = this.financialEventCandidateService.findById(candidateId);
        final var investment = this.investmentService.findById(candidate.getInvestmentId());
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
