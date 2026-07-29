package com.mageddo.gradletestreports.entrypoint;

import java.util.List;
import java.util.Locale;

import jakarta.inject.Singleton;

import com.mageddo.gradletestreports.project.GradleProject;
import com.mageddo.gradletestreports.testresult.TestCase;
import com.mageddo.gradletestreports.testresult.TestResultSet;

@Singleton
public class TestReportsMapper {

  public List<ProjectRes> toProjectRes(final List<GradleProject> projects) {
    return projects.stream()
        .map(this::toProjectRes)
        .toList();
  }

  public List<TestResultSetRes> toResultSetRes(final List<TestResultSet> resultSets) {
    return resultSets.stream()
        .map(this::toResultSetRes)
        .toList();
  }

  public List<TestCaseRes> toTestCaseRes(final List<TestCase> cases) {
    return cases.stream()
        .map(this::toTestCaseRes)
        .toList();
  }

  private ProjectRes toProjectRes(final GradleProject project) {
    return ProjectRes.builder()
        .name(project.getName())
        .path(project.getPath().toString())
        .build();
  }

  private TestResultSetRes toResultSetRes(final TestResultSet resultSet) {
    return TestResultSetRes.builder()
        .name(resultSet.getName())
        .totalTimeSeconds(resultSet.getTotalTimeSeconds())
        .total(resultSet.getTotal())
        .passed(resultSet.getPassed())
        .skipped(resultSet.getSkipped())
        .failed(resultSet.getFailed())
        .build();
  }

  private TestCaseRes toTestCaseRes(final TestCase testCase) {
    return TestCaseRes.builder()
        .cls(testCase.getClassName())
        .pkg(testCase.getPackageName())
        .test(testCase.getName())
        .dur(String.format(Locale.US, "%.3fs", testCase.getDurationSeconds()))
        .sec(testCase.getDurationSeconds())
        .result(testCase.getOutcome().label())
        .build();
  }
}
