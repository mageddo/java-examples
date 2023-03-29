package com.mageddo.springexamples;

import com.mageddo.springexamples.ex01.Root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class App {
  public static void main(String[] args) {
    final var ctx = SpringApplication.run(App.class, args);

    final var root = ctx.getBean(Root.class);
    root.run();
  }
}
