package com.mageddo.coffeemaker.checkout;

import com.mageddo.MicrometerOtlpRegistryConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.opentelemetry.api.GlobalOpenTelemetry;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.mageddo")
@Import(MicrometerOtlpRegistryConfig.class)
public class Main {

  public static void main(String[] args) {

    final var openTelemetry = GlobalOpenTelemetry.get();
    System.out.println(openTelemetry);

    SpringApplication.run(Main.class, args);
  }

}
