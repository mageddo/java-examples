package com.mageddo.vendor.jira;

import com.mageddo.vendor.jira.apiclient.IssueApiClient;
import com.mageddo.vendor.jira.apiclient.IssueChangelogRes;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class IssueAppService {

  private final IssueApiClient apiClient;

  public IssueStatusDuration find(String issueKey) {
    final var transitions = findAllStatusTransitions(issueKey);
    return IssueStatusDuration.builder()
        .issueKey(issueKey)
        .statusTimes(calcStatusTimes(transitions))
        .build();
  }

  private List<IssueChangelogRes.Changelog> findAllStatusTransitions(String issueKey) {
    final var result = new ArrayList<IssueChangelogRes.Changelog>();
    var startAt = 0;
    IssueChangelogRes page;
    do {
      page = this.apiClient.findChangelog(issueKey, startAt);
      page.values().stream()
          .filter(c -> c.items().stream().anyMatch(i -> "status".equals(i.field())))
          .forEach(result::add);
      startAt += page.maxResults();
    } while (!Boolean.TRUE.equals(page.isLast()));
    return result;
  }

  private List<StatusTime> calcStatusTimes(List<IssueChangelogRes.Changelog> transitions) {
    final Map<String, Duration> accumulated = new LinkedHashMap<>();
    for (int i = 0; i < transitions.size(); i++) {
      final var current = transitions.get(i);
      final var statusItem = current.items().stream()
          .filter(it -> "status".equals(it.field()))
          .findFirst()
          .orElseThrow();
      final var status = statusItem.fromString();
      final var enteredAt = current.created();
      final var leftAt = i + 1 < transitions.size()
          ? transitions.get(i + 1).created()
          : null;
      if (leftAt != null) {
        accumulated.merge(status, Duration.between(enteredAt, leftAt), Duration::plus);
      }
    }
    return accumulated.entrySet().stream()
        .map(e -> StatusTime.builder().status(e.getKey()).duration(e.getValue()).build())
        .toList();
  }
}

