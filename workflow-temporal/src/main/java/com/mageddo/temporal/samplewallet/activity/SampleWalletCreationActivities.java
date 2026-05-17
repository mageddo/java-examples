package com.mageddo.temporal.samplewallet.activity;

import com.mageddo.temporal.samplewallet.domain.SampleWalletCreationResult;
import java.util.List;
import java.util.Map;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SampleWalletCreationActivities {

  @ActivityMethod
  String createWallet(String investorId);

  @ActivityMethod
  List<String> createInvestments(String investorId, String walletId);

  @ActivityMethod
  List<String> createFinancialEventCandidates(String workflowId, String walletId, List<String> investmentIds);

  @ActivityMethod
  Map<String, Boolean> fanOutCandidates(List<String> candidateIds);

  @ActivityMethod
  SampleWalletCreationResult finishSampleWalletCreation(String walletId, List<String> investmentIds, List<String> candidateIds);

  @ActivityMethod
  void abortSampleWalletCreation(String walletId);
}
