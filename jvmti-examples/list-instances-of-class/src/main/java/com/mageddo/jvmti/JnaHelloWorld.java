package com.mageddo.jvmti;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class JnaHelloWorld {
  public static void main(String[] args) {
    FruitInstanceMaker.makeInstances(7);
//    System.out.println(JvmtiInstanceCounter.INSTANCE.countInstances(Fruit.class));
    System.out.println(JvmtiInstanceCounter.INSTANCE.sum(5, 7));

  }

  public interface JvmtiInstanceCounter extends Library {
    JvmtiInstanceCounter INSTANCE = Native.load(
      "H:\\jogos-com-backup\\java-examples\\jvmti-examples\\list-instances-of-class\\build\\libs\\jvmtiInstanceCounter\\shared\\jvmtiInstanceCounter.dll",
      JvmtiInstanceCounter.class
    );

    //    int countInstances(Class jclass);
    int sum(int a, int b);
  }
}
