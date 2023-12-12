package com.mageddo.shardingsphere.infrastructure;

import org.springframework.context.ApplicationContext;

public class AppContext{

  private static ApplicationContext context;

  public AppContext(ApplicationContext context) {
    AppContext.context = context;
  }

  public static ApplicationContext context() {
    return context;
  }
}
