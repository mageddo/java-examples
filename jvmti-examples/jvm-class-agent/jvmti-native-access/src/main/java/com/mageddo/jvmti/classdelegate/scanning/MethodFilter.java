package com.mageddo.jvmti.classdelegate.scanning;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MethodFilter {
  String methodName;
  List<MethodArgument> arguments;
  String returnValue;
  List<Rule> rules;
}
