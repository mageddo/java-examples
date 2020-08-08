package com.mageddo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@QuarkusMain
public class QuarkusApp {
  public static void main(String[] args) {
    log.info("starting-app");
    Quarkus.run(args);
  }
}
