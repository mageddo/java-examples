package com.mageddo.coffeemaker.checkout;

import com.mageddo.MicrometerOtlpRegistryConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.mageddo")
@Import(MicrometerOtlpRegistryConfig.class)
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  /**
   * @param configurer will ensure `traceparent` param propagation.
   * @see https://stackoverflow.com/a/75175448/2979435
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilderConfigurer configurer){
    return configurer.configure(new RestTemplateBuilder()).build();
  }

}
