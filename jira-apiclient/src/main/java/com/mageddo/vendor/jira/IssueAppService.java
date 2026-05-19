package com.mageddo.vendor.jira;

import com.mageddo.vendor.jira.apiclient.IssueApiClient;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class IssueAppService {

  private final IssueApiClient apiClient;

  public IssueStatusDuration find(String issueKey){
    throw new UnsupportedOperationException();
  }

}
