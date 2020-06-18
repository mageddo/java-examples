package com.mageddo.jvmti.classdelegate.scanning;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class InstanceFilter {

  List<FieldFilter> fieldFilters = new ArrayList<>();
  List<MethodFilter> methodFilters = new ArrayList<>();

  public InstanceFilter addFilter(FieldFilter filter){
    this.fieldFilters.add(filter);
    return this;
  }

  public InstanceFilter addFilter(MethodFilter filter){
    this.methodFilters.add(filter);
    return this;
  }
}
