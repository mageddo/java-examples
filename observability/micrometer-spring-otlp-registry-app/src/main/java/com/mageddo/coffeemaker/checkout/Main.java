package com.mageddo.coffeemaker.checkout;

import com.mageddo.coffeemaker.checkout.configurer.archive.MicrometerOtlpRegistryConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = "com.mageddo")
@Import(MicrometerOtlpRegistryConfig.class)
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
