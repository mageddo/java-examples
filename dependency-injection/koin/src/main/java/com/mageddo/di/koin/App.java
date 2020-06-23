package com.mageddo.di.koin;

import org.koin.core.KoinApplication;
import org.koin.core.module.Module;
import org.koin.core.module.ModuleKt;
import org.koin.java.KoinJavaComponent;

public class App {
  public static void main(String[] args) {
    final FruitDeliveryResource instance =
        KoinJavaComponent.get(FruitDeliveryResource.class);
    System.out.println(instance);
    //    KoinApplication.init()
//        .printLogger()
//        .modules(new Module(true, true).scope(););
  }
}
