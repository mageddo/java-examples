package com.mageddo.gradletestreports.testresult;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestResultSet {

  String name;
  double totalTimeSeconds;
  int total;
  int passed;
  int skipped;
  int failed;

  @Override
  public String toString() {
    return "TestResultSet(name=" + this.name + ", total=" + this.total + ")";
  }
}
