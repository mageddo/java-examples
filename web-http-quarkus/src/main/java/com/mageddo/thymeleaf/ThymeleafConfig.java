package com.mageddo.thymeleaf;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class ThymeleafConfig {
  @Singleton
  @Produces
  public Thymeleaf thymeleaf(
      @ConfigProperty(name = "thymeleaf.cache.enabled", defaultValue = "false") boolean cacheable
  ){
    return new Thymeleaf(cacheable);
  }
}
