package com.mageddo.di.dagger2.app;

import com.mageddo.di.dagger2.core.CoreModule;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {CoreModule.class})
public interface AppFactory {

  void inject(App app);

  FruitDeliveryResource fruitDeliveryResource();

  static AppFactory getInstance(){
    return DaggerAppFactory.create();
  }
}
