package com.mageddo.jvmti.classdelegate.scanning;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class FieldFilter {

  String fieldName;

  List<Rule> rules;

  public static class FieldFilterBuilder {
    public FieldFilterBuilder() {
      this.rules = new ArrayList<>();
    }

    public FieldFilterBuilder addRule(Rule r){
      this.rules.add(r);
      return this;
    }
  }
}
