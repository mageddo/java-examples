package com.mageddo.gradletestreports.testresult;

import java.util.List;

import jakarta.inject.Singleton;

@Singleton
public class TestResultSetFactory {

  public TestResultSet of(final String name, final List<TestCase> cases) {
    return TestResultSet.builder()
        .name(name)
        .total(cases.size())
        .passed(this.count(cases, TestOutcome.PASSED))
        .skipped(this.count(cases, TestOutcome.SKIPPED))
        .failed(this.count(cases, TestOutcome.FAILED))
        .totalTimeSeconds(this.totalTime(cases))
        .build();
  }

  private int count(final List<TestCase> cases, final TestOutcome outcome) {
    return (int) cases.stream()
        .filter(testCase -> testCase.getOutcome() == outcome)
        .count();
  }

  private double totalTime(final List<TestCase> cases) {
    return cases.stream()
        .mapToDouble(TestCase::getDurationSeconds)
        .sum();
  }
}
