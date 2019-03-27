package com.mageddo.kafka;

import com.mageddo.kafka.producer.MessageSender;
import com.mageddo.kafka.producer.MessageSenderImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(Config.class)
@EnableKafka
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class KafkaStarter {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStarter.class, args);
	}
	@Bean
	public MessageSender messageSender(KafkaTemplate<String, byte[]> template) {
		return new MessageSenderImpl(template);
	}

}
