package com.mageddo.jvmti.classdelegate.scanning.rules;

public interface Rule<V> {
  boolean matches(V value);
}
