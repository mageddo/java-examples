package com.mageddo.proguard;

import java.time.LocalDateTime;

public class HelloPrinter {
  public void sayHello() {
    if (this.getClass().getName().equals("HelloPrinter")) {
      System.out.println("oi!");
    } else {
      System.out.println("Hello World!!!");
      System.out.println(LocalDateTime.now());
    }
  }
}
