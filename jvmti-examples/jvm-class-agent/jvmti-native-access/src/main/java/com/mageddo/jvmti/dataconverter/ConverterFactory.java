package com.mageddo.jvmti.dataconverter;

import com.mageddo.jvmti.classdelegate.InstanceId;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

  private static final Map<Pair, Converter> converters = new HashMap<>();

  static {
    converters.put(Pair.of(String.class, Integer.class), new StringToIntegerConverter());
    converters.put(Pair.of(String.class, Double.class), new StringToDoubleConverter());
    converters.put(Pair.of(String.class, InstanceId.class), new StringToInstanceIdConverter());
  }

  public static <From, To> To convert(From from, Class<To> to) {
    if(from.getClass().equals(to)){
      return (To) from;
    }
    final Pair pair = Pair.of(from.getClass(), to);
    if(!converters.containsKey(pair)){
      throw new IllegalArgumentException(String.format("convert not found for: %s", pair));
    }
    return (To) converters.get(pair).convert(from);
  }

  @Value
  static class Pair {

    Class from;
    Class to;

    public static Pair of(Class from, Class to) {
      return new Pair(from, to);
    }
  }
}
