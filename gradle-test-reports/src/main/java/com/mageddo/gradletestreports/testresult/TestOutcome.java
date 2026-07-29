package com.mageddo.gradletestreports.testresult;

public enum TestOutcome {

  PASSED("passed"),
  SKIPPED("skipped"),
  FAILED("failed");

  private final String label;

  TestOutcome(final String label) {
    this.label = label;
  }

  public String label() {
    return this.label;
  }
}
