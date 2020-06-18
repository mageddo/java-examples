package com.mageddo.jvmti.classdelegate.scanning.rules;

import lombok.SneakyThrows;

public class RulesFactory {
  @SneakyThrows
  public static Rule create(String rule, String value) {
    final Class<?> ruleClass = Class.forName(rule);
    if (EqualToRule.class.equals(ruleClass)) {
      return new EqualToRule(value);
    } else if(GreaterEqualThanRule.class.equals(ruleClass)){
      return new GreaterEqualThanRule(Double.parseDouble(value));
    }
    throw new UnsupportedOperationException(String.format("not rule for: %s, value: %s", rule, value));
  }
}
