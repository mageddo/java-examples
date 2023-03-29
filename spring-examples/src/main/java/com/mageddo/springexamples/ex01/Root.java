package com.mageddo.springexamples.ex01;

import java.util.Map;

import com.mageddo.springexamples.ex01.country.Country;
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

    System.out.println();
    System.out.println("> Find beans with specific annotation");
    final var beans = this.context.getBeansWithAnnotation(CountryComp.class);
    System.out.println(beans);
    System.out.println();

    System.out.println("> Find specific bean by name and type");
    final var country = this.context.getBean("brazil", Country.class);
    System.out.println(country);
    System.out.println();

    System.out.println("> Props to object");
    final var props = this.context.getBean(Props.class);
    System.out.println(props.getProps());

    System.out.println();
    for (final String bean : props.getProps().keySet()) {
      final var reporterMap = (Map<String, Object>) props.getProps().get(bean);
      for (final String type : reporterMap.keySet()) {
        final var v = reporterMap.get(type);
        System.out.printf("name: %s, type=%s, values=%s%n", bean, type, v);
      }
    }


  }
}
