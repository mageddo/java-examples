package com.mageddo.jvmti.classdelegate.scanning.rules;

import com.mageddo.jvmti.classdelegate.ObjectReference;

public class GreaterEqualThanRule implements Rule<ObjectReference> {

  private final Number expected;

  public GreaterEqualThanRule(Number expected) {
    this.expected = expected;
  }

  @Override
  public boolean matches(ObjectReference value) {
    return Double.compare(this.expected.doubleValue(), value.asDouble()) >= 0;
  }
}
