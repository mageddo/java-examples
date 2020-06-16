package com.mageddo.jvmti;

import java.util.ArrayList;
import java.util.List;

public class HelloWorld {

  public static void main(String[] args) {

    List instances=  new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      instances.add(new HelloWorld());
    }

    System.out.println("Hello World from " + HelloWorld.class.getName());
    System.out.println("instances: " + countInstances(HelloWorld.class));
  }

  private static native int countInstances(Class klass);
}