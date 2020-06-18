package com.mageddo.jvmti.agents.entrypoint.vos;

import lombok.Data;

import java.util.List;

@Data
public class InstanceFilterReq {
  List<FieldFilterReq> fieldFilters;
  List<MethodFilterReq> methodFilters;
}
