package com.mageddo.gradletestreports.entrypoint;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectRes {

  String name;
  String path;
}
