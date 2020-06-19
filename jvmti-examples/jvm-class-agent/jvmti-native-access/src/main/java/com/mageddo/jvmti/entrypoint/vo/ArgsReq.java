package com.mageddo.jvmti.entrypoint.vo;

import com.mageddo.jvmti.InstanceValue;
import com.mageddo.jvmti.dataconverter.ConverterFactory;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class ArgsReq {

  String value;
  String clazz;

  public static List<Object> toArgs(List<ArgsReq> args) {
    if(args == null){
      return Collections.emptyList();
    }
    return args
      .stream()
      .map(ArgsReq::toArg)
      .collect(Collectors.toList());
  }

  public static List<InstanceValue> toInstanceValues(List<ArgsReq> args) {
    return args
      .stream()
      .map(it -> InstanceValue.of(it.toArg()))
      .collect(Collectors.toList());
  }

  @SneakyThrows
  public Object toArg() {
    return ConverterFactory.convert(this.value, Class.forName(this.clazz));
  }
}
