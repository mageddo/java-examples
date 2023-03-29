package com.mageddo.springexamples.ex01;

import com.mageddo.springexamples.ex01.country.CountryComp;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Root {

  private final ApplicationContext context;

  public Root(ApplicationContext context) {
    this.context = context;
  }

  public void run(){

    // get beans with specific annotation
    final var beans = this.context.getBeansWithAnnotation(CountryComp.class);
    System.out.println(beans);

  }
}
