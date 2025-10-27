package com.pagbank.logging;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class App implements InitializingBean {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void afterPropertiesSet() {
    log.info("action=helloWorld");
  }
}
