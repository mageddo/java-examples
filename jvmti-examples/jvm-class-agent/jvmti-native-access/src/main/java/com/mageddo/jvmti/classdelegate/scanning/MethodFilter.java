package com.mageddo.jvmti.classdelegate.scanning;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
@Builder
public class MethodFilter {

  String methodName;

  @Builder.Default
  List<Object> arguments = Collections.emptyList();

  List<Rule> rules;

  public static class MethodFilterBuilder {

    public MethodFilterBuilder() {
      this.rules = new ArrayList<>();
    }

    public MethodFilterBuilder addRule(Rule r) {
      this.rules.add(r);
      return this;
    }
  }
}
