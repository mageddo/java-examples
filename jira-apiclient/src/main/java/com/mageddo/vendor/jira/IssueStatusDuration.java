package com.mageddo.vendor.jira;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class IssueStatusDuration {

  @NonNull
  String issueKey;

  @NonNull
  List<StatusTime> statusTimes;
}
