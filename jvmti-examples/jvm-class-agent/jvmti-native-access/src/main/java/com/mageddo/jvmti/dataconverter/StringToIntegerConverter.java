package com.mageddo.jvmti.dataconverter;

public class StringToIntegerConverter implements Converter<String, Integer> {
  @Override
  public Integer convert(String s) {
    return Integer.parseInt(s);
  }
}
