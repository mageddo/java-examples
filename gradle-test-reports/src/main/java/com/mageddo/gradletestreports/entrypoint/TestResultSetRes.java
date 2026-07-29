package com.mageddo.gradletestreports.entrypoint;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestResultSetRes {

  String name;
  double totalTimeSeconds;
  int total;
  int passed;
  int skipped;
  int failed;
}
