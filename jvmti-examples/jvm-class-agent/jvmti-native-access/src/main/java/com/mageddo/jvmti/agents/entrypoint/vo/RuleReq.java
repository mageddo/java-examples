package com.mageddo.jvmti.agents.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.scanning.rules.Rule;
import com.mageddo.jvmti.classdelegate.scanning.rules.RulesFactory;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class RuleReq {

  String value;
  String rule;

  public static List<Rule> toRules(List<RuleReq> rules) {
    return rules
      .stream()
      .map(RuleReq::toRule)
      .collect(Collectors.toList());
  }

  public Rule toRule() {
    return RulesFactory.create(this.rule, this.value);
  }
}
