package com.mageddo.jvmti.classdelegate.scanning;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MethodArgument {
  private String value;
  private Class<?> type;
}
