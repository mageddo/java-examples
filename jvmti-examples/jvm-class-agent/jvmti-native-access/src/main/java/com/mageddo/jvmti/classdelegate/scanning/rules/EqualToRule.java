package com.mageddo.jvmti.classdelegate.scanning.rules;

import com.mageddo.jvmti.classdelegate.ObjectReference;

public class EqualToRule implements Rule<ObjectReference> {

  private final Object expected;

  public EqualToRule(Object expected) {
    this.expected = expected;
  }

  @Override
  public boolean matches(ObjectReference value) {
    return String.valueOf(this.expected).equals(value.asText());
  }
}
