package com.mageddo.vendor.jira;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Duration;

@Value
@Builder
public class StatusTime {

  @NonNull
  String status;

  @NonNull
  Duration duration;
}
