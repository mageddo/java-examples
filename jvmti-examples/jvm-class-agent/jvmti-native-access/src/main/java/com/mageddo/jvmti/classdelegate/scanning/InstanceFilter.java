package com.mageddo.jvmti.classdelegate.scanning;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class InstanceFilter {

  List<FieldFilter> fieldFilters;
  List<MethodFilter> methodFilters;

}
