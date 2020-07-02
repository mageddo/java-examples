package com.mageddo.kafka.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableKafka
@EnableScheduling
@SpringBootApplication
public class Main {

  public static final int EVERY_FIVE_SECONDS = 5 * 1000;

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final KafkaTemplate<String, String> kafkaTemplate;

  public Main(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Scheduled(fixedDelay = EVERY_FIVE_SECONDS)
  void createFruits(){
    this.kafkaTemplate.send("fruit", new Fruit().setName("Orange").toString());
    logger.info("post-fruit-to-kafka");
  }
}
