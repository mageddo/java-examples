package com.mageddo.di.dagger2.core;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface CoreModule {
  @Singleton
  @Binds
  FruitDeliveryDAO fruitDeliveryDao (FruitDeliveryDAOStdout impl);
}
