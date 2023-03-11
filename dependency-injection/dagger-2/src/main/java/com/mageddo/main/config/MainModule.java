package com.mageddo.main.config;

import javax.inject.Singleton;

import com.mageddo.main.FruitDAO;
import com.mageddo.main.FruitDAOStdout;

import dagger.Binds;
import dagger.Module;

@Module
public interface MainModule {
  @Binds
  @Singleton
  FruitDAO bind(FruitDAOStdout impl);
}
