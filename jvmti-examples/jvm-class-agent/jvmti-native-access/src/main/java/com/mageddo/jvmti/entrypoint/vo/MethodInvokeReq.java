package com.mageddo.jvmti.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.InstanceId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class MethodInvokeReq {
  InstanceId instanceId;
  String name;
  List<ArgsReq> args;
}
