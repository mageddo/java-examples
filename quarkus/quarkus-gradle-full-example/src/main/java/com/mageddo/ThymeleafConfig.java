package com.mageddo;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.mageddo.thymeleaf.Thymeleaf;

public class ThymeleafConfig {
  @Singleton
  @Produces
  public Thymeleaf thymeleaf(){
    return new Thymeleaf(true);
  }
}
