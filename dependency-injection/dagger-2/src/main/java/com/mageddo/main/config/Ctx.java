package com.mageddo.main.config;

import javax.inject.Singleton;


import com.mageddo.main.FruitDeliveryResource;

import dagger.Component;

@Singleton
@Component(
    modules = {
        MainModule.class,
        SecondaryModule.class
    }
)
public interface Ctx {

  FruitDeliveryResource fruitResource();

  static Ctx create(){
    return DaggerCtx.create();
  }
}
