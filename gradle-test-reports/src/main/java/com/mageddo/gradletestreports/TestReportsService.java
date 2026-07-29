package com.mageddo.gradletestreports;

import java.nio.file.Path;
import java.util.List;

import jakarta.inject.Singleton;

import com.mageddo.gradletestreports.project.GradleProject;
import com.mageddo.gradletestreports.project.GradleProjectRepo;
import com.mageddo.gradletestreports.testresult.TestCase;
import com.mageddo.gradletestreports.testresult.TestResultRepo;
import com.mageddo.gradletestreports.testresult.TestResultSet;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class TestReportsService {

  private final GradleProjectRepo gradleProjectRepo;
  private final TestResultRepo testResultRepo;

  public List<GradleProject> findProjects(final Path dir) {
    return this.gradleProjectRepo.findProjects(dir);
  }

  public List<TestResultSet> findResultSets(final Path projectDir) {
    return this.testResultRepo.listResultSets(projectDir);
  }

  public List<TestCase> findTestCases(final Path projectDir, final String resultName) {
    return this.testResultRepo.listTestCases(projectDir, resultName);
  }
}
