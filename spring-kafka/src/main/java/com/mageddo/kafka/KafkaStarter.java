package com.mageddo.kafka;

import com.mageddo.kafka.producer.MessageSender;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@Import(Config.class)
@EnableKafka
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class KafkaStarter {

	public static void main(String[] args) {

		final ConfigurableApplicationContext ctx = SpringApplication.run(KafkaStarter.class, args);

		final MessageSender messageSender = ctx.getBean(MessageSender.class);
		messageSender.send(new ProducerRecord<>(
			TopicEnum.COFFEE_REQUEST.getTopic().getName(),
			String.valueOf(LocalDateTime.now()).getBytes()
		));
	}
}
