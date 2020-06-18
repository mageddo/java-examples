package com.mageddo.jvmti.agents.entrypoint.vo;

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

  @SneakyThrows
  public Object toArg() {
    return ConverterFactory.convert(this.value, Class.forName(this.clazz));
  }
}
