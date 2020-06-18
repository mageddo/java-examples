package com.mageddo.jvmti.classdelegate.scanning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceFilter {

  @Builder.Default
  List<FieldFilter> fieldFilters = new ArrayList<>();

  @Builder.Default
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
