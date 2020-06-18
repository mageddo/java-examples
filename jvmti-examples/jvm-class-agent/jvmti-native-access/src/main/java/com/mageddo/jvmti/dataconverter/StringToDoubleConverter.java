package com.mageddo.jvmti.dataconverter;

public class StringToDoubleConverter implements Converter<String, Double> {
  @Override
  public Double convert(String s) {
    return Double.parseDouble(s);
  }
}
