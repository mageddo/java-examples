package com.mageddo.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(Config.class)
@EnableKafka
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class KafkaAvroStarter {

	public static void main(String[] args) {
		SpringApplication.run(KafkaAvroStarter.class, args);
	}

}
