package com.mageddo.runtimejarloading.helloworldlibrary;

public class HelloWorldLibrary {
  public String helloWorld() {
    return this.getClass().getName() + ": Hello World!!!";
  }
}
