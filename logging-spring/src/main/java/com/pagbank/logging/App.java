package com.pagbank.logging;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class App {
  public static void main(String[] args) {
    log.info("action=helloWorld");
  }
}
