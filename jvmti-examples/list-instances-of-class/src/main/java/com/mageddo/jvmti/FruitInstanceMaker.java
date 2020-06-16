package com.mageddo.jvmti;

import java.util.ArrayList;
import java.util.List;

public class FruitInstanceMaker {

  private static final List<Fruit> instances = new ArrayList<>();

  public static void makeInstances(int howMany){
    for (int i = 0; i < howMany; i++) {
      instances.add(new Fruit());
    }
  }
}
