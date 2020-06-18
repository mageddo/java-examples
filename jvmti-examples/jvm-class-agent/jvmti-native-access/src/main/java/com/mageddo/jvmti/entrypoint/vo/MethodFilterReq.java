package com.mageddo.jvmti.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.scanning.MethodFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class MethodFilterReq {

  String name;
  List<ArgsReq> args;
  List<RuleReq> rules;

  public static List<MethodFilter> toMethodFilters(List<MethodFilterReq> methodFilters) {
    return methodFilters
      .stream()
      .map(MethodFilterReq::toMethodFilter)
      .collect(Collectors.toList());
  }

  public MethodFilter toMethodFilter() {
    return MethodFilter
      .builder()
      .methodName(this.name)
      .arguments(ArgsReq.toArgs(this.args))
      .rules(RuleReq.toRules(this.rules))
      .build();
  }
}
