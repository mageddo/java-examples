package com.mageddo.coffeemaker.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.mageddo")
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
