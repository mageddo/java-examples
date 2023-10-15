package com.mageddo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TracingConfig implements InitializingBean {

  private final KafkaTemplate kafkaTemplate;

  @Override
  public void afterPropertiesSet() throws Exception {
    // see https://stackoverflow.com/a/74876701/2979435
    this.kafkaTemplate.setObservationEnabled(true);
  }
}
