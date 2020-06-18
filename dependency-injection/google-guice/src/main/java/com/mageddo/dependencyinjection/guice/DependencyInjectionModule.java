package com.mageddo.dependencyinjection.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class DependencyInjectionModule extends AbstractModule {

  @Provides
  static HelloDAO helloDAO(){
    return new HelloDAO();
  }

  @Provides
  static HelloService helloService(HelloDAO helloDAO){
    return new HelloService(helloDAO);
  }

  @Provides
  static HelloResource helloResource(HelloService helloService){
    return new HelloResource(helloService);
  }
}
