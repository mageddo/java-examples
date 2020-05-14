package com.mageddo.classpath;

import com.mageddo.fruitcase.Fruit;

import org.apache.commons.lang3.EnumUtils;

public class Main {
  public static void main(String[] args) {
    System.out.printf("enum utils = %s%n", EnumUtils.newMethod());
    System.out.printf("fruit class version = %s%n", Fruit.classVersion());
  }
}
