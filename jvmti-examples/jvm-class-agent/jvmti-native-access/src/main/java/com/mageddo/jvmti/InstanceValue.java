package com.mageddo.jvmti;

import com.mageddo.jvmti.classdelegate.InstanceId;
import com.mageddo.jvmti.dataconverter.ConverterFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
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

  public static InstanceValue of(InstanceId id){
    return new InstanceValue(id, ClassId.of(InstanceId.class), String.valueOf(id.getCode()));
  }

  @SneakyThrows
  public Object toArg() {
    return ConverterFactory.convert(this.value, classId.toClass());
  }
}
