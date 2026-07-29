package com.mageddo.gradletestreports.testresult;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestCase {

  String className;
  String packageName;
  String name;
  double durationSeconds;
  TestOutcome outcome;

  @Override
  public String toString() {
    return "TestCase(className=" + this.className + ", outcome=" + this.outcome + ")";
  }
}
