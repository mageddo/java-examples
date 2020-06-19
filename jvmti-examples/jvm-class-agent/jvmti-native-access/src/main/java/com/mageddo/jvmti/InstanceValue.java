package com.mageddo.jvmti;

import com.mageddo.jvmti.classdelegate.InstanceId;
import com.mageddo.jvmti.dataconverter.ConverterFactory;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@Builder
public class InstanceValue {

  InstanceId id;
  ClassId classId;
  String value;

  public static InstanceValue of(Object instance) {
    return new InstanceValue(
      InstanceId.of(instance),
      ClassId.of(instance.getClass().getName()),
      String.valueOf(instance)
    );
  }

  @SneakyThrows
  public Object toArg() {
    return ConverterFactory.convert(this.value, classId.toClass());
  }
}
