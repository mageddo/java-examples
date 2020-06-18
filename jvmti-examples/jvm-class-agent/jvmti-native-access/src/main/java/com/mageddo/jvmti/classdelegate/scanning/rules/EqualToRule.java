package com.mageddo.jvmti.classdelegate.scanning.rules;

import com.mageddo.jvmti.classdelegate.scanning.Rule;

public class EqualToRule implements Rule<Object> {

  private final Object expected;

  public EqualToRule(Object expected) {
    this.expected = expected;
  }

  @Override
  public boolean matches(Object value) {
    return String.valueOf(this.expected).equals(String.valueOf(value));
  }
}
