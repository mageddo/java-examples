package com.mageddo.jvmti.dataconverter;

public interface Converter<From, To> {
  To convert(From from);
}
