package com.mageddo.dagger;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Component;
import dagger.Module;

@Singleton
@Component(
    modules = {
        AppConfig.Modules.class
    }
)
public interface AppConfig {

  FruitDeliveryResource fruitResource();

  @Module
  interface Modules {
    @Binds
    @Singleton
    FruitDAO bind(FruitDAOStdout impl);
  }

}
