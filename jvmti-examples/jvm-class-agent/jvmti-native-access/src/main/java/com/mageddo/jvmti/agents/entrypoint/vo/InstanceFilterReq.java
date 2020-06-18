package com.mageddo.jvmti.agents.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import lombok.Data;

import java.util.List;

@Data
public class InstanceFilterReq {

  List<FieldFilterReq> fieldFilters;
  List<MethodFilterReq> methodFilters;

  public InstanceFilter toInstanceFilter() {
    throw new UnsupportedOperationException();
  }
}
