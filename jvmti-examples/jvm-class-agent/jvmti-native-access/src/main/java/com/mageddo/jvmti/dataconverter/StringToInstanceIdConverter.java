package com.mageddo.jvmti.dataconverter;

import com.mageddo.jvmti.classdelegate.InstanceId;

import java.util.UUID;

public class StringToInstanceIdConverter implements Converter<String, InstanceId> {
  @Override
  public InstanceId convert(String o) {
    return InstanceId.of(UUID.fromString(o));
  }
}
