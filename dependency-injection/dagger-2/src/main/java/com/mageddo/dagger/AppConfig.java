package com.mageddo.dagger;

import dagger.Binds;
import dagger.Component;
import dagger.Module;

import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        AppConfig.FruitResourceModule.class
    }
)
public interface AppConfig {

  FruitDeliveryResource fruitResource();

  @Module
  interface FruitResourceModule {
    @Binds
    @Singleton
    FruitDAO bindFruitResource(FruitDAOStdout impl);
  }

}
