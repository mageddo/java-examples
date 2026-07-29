package com.mageddo.gradletestreports.project;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import jakarta.inject.Singleton;

@Singleton
public class GradleProjectRepo {

  private static final Set<String> BUILD_FILES = Set.of("build.gradle", "build.gradle.kts");
  private static final Set<String> IGNORED_DIRS = Set.of("build", ".gradle", ".git", "node_modules");
  private static final int MAX_DEPTH = 4;

  public List<GradleProject> findProjects(final Path dir) {
    if (!Files.isDirectory(dir)) {
      return List.of();
    }
    try (final var stream = Files.walk(dir, MAX_DEPTH)) {
      return this.toProjects(stream, dir);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private List<GradleProject> toProjects(final Stream<Path> stream, final Path root) {
    return stream
        .filter(this::isBuildFile)
        .map(Path::getParent)
        .filter(path -> this.isNotIgnored(root, path))
        .distinct()
        .sorted(Comparator.comparing(Path::toString))
        .map(this::toProject)
        .toList();
  }

  private GradleProject toProject(final Path projectDir) {
    return GradleProject.builder()
        .name(projectDir.getFileName().toString())
        .path(projectDir.toAbsolutePath().normalize())
        .build();
  }

  private boolean isBuildFile(final Path path) {
    return Files.isRegularFile(path) && BUILD_FILES.contains(path.getFileName().toString());
  }

  private boolean isNotIgnored(final Path root, final Path projectDir) {
    final var relative = root.relativize(projectDir);
    for (final var segment : relative) {
      if (IGNORED_DIRS.contains(segment.toString())) {
        return false;
      }
    }
    return true;
  }
}
