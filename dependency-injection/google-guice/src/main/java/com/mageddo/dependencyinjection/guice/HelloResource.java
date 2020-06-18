package com.mageddo.dependencyinjection.guice;

import javax.inject.Singleton;

@Singleton
public class HelloResource {

  private final HelloService helloService;

  public HelloResource(HelloService helloService) {
    this.helloService = helloService;
  }

  public void sayHello(){
    System.out.println(this.helloService.sayHello());
  }
}
