package com.mageddo.shardingsphere;

import com.mageddo.shardingsphere.infrastructure.AppContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public AppContext appContext(ApplicationContext context){
    return new AppContext(context);
  }
}
