package com.mageddo.jvmti.entrypoint.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MethodInvokeReq {
  int instanceHashCode;
  String name;
  List<ArgsReq> args;
}
