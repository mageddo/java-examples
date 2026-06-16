package com.mageddo.coffeemaker.checkout.configurer;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfigurer {
  /**
   * @param configurer will ensure `traceparent` param propagation.
   * @see https://stackoverflow.com/a/75175448/2979435
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilderConfigurer configurer){
    return configurer.configure(new RestTemplateBuilder()).build();
  }
}
