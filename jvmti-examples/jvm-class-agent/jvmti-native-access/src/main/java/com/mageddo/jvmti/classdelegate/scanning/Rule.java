package com.mageddo.jvmti.classdelegate.scanning;

public interface Rule<V> {
  boolean matches(V value);
}
