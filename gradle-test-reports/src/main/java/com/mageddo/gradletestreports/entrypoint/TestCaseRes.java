package com.mageddo.gradletestreports.entrypoint;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestCaseRes {

  String cls;
  String pkg;
  String test;
  String dur;
  double sec;
  String result;
}
