package com.mageddo.gradletestreports.project;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GradleProject {

  String name;
  Path path;

  @Override
  public String toString() {
    return "GradleProject(name=" + this.name + ")";
  }
}
