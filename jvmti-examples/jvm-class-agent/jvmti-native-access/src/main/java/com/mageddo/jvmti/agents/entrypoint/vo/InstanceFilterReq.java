package com.mageddo.jvmti.agents.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class InstanceFilterReq {

  List<FieldFilterReq> fieldFilters;
  List<MethodFilterReq> methodFilters;

  public InstanceFilter toInstanceFilter() {
    return InstanceFilter
      .builder()
      .fieldFilters(FieldFilterReq.toFieldFilters(this.fieldFilters))
      .methodFilters(MethodFilterReq.toMethodFilters(this.methodFilters))
      .build();
  }
}
