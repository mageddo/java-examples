package com.mageddo.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import(Config.class)
@EnableKafka
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class KafkaStarter {
	public static void main(String[] args) {
		SpringApplication.run(KafkaStarter.class, args);
	}
}
