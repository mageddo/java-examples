package com.mageddo.gradletestreports.testresult;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import jakarta.inject.Singleton;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class TestResultRepo {

  private static final String TEST_RESULTS_DIR = "build/test-results";

  private final JunitXmlParser parser;
  private final TestResultSetFactory factory;

  public List<TestResultSet> listResultSets(final Path projectDir) {
    final var resultsDir = projectDir.resolve(TEST_RESULTS_DIR);
    if (!Files.isDirectory(resultsDir)) {
      return List.of();
    }
    try (final var stream = Files.list(resultsDir)) {
      return this.toResultSets(projectDir, stream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public List<TestCase> listTestCases(final Path projectDir, final String resultName) {
    final var resultDir = projectDir.resolve(TEST_RESULTS_DIR).resolve(resultName);
    if (!Files.isDirectory(resultDir)) {
      return List.of();
    }
    try (final var stream = Files.list(resultDir)) {
      return this.parseCases(stream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private List<TestResultSet> toResultSets(final Path projectDir, final Stream<Path> stream) {
    return stream
        .filter(this::hasReports)
        .sorted(Comparator.comparing(path -> path.getFileName().toString()))
        .map(dir -> this.factory.of(
            dir.getFileName().toString(),
            this.listTestCases(projectDir, dir.getFileName().toString())
        ))
        .toList();
  }

  private List<TestCase> parseCases(final Stream<Path> stream) {
    return stream
        .filter(this::isReport)
        .flatMap(xml -> this.parser.parseCases(xml).stream())
        .toList();
  }

  private boolean hasReports(final Path dir) {
    if (!Files.isDirectory(dir)) {
      return false;
    }
    try (final var stream = Files.list(dir)) {
      return stream.anyMatch(this::isReport);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private boolean isReport(final Path path) {
    final var fileName = path.getFileName().toString();
    return Files.isRegularFile(path) && fileName.startsWith("TEST-") && fileName.endsWith(".xml");
  }
}
