package com.mageddo.jvmti.agents.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.scanning.FieldFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class FieldFilterReq {
  String name;
  List<RuleReq> rules;

  public static List<FieldFilter> toFieldFilters(List<FieldFilterReq> fieldFilters) {
    return fieldFilters
      .stream()
      .map(FieldFilterReq::toFieldFilter)
      .collect(Collectors.toList())
      ;
  }

  public FieldFilter toFieldFilter() {
    return FieldFilter
      .builder()
      .fieldName(this.getName())
      .rules(RuleReq.toRules(this.rules))
      .build()
      ;
  }
}
