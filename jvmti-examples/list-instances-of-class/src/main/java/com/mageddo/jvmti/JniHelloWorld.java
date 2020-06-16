package com.mageddo.jvmti;

public class JniHelloWorld {

  public static void main(String[] args) {
    FruitInstanceMaker.makeInstances(5);
    System.out.println("Hello World from " + JniHelloWorld.class.getName());
    System.out.println("instances: " + countInstances(JniHelloWorld.class));
  }

  private static native int countInstances(Class klass);
}