package com.mageddo.kafka.consumer;

import com.mageddo.kafka.TopicConsumer;
import com.mageddo.kafka.TopicDefinition;
import com.mageddo.kafka.TopicEnum;
import com.mageddo.kafka.producer.MessageSender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

import static com.mageddo.kafka.TopicEnum.Constants.COFFEE_REQUEST_FACTORY;

@Component
public class CoffeeMakerConsumer implements TopicConsumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final MessageSender messageSender;
	private final Random random;

	public CoffeeMakerConsumer(MessageSender messageSender) {
		this.messageSender = messageSender;
		this.random = new Random();
	}

	@KafkaListener(topics = "#{__listener.topic().getName()}", containerFactory = COFFEE_REQUEST_FACTORY)
	public void consume(ConsumerRecord<?, byte[]> record) throws InterruptedException {
		messageSender.send(new ProducerRecord<>(topic().getName(), String.valueOf(LocalDateTime.now()).getBytes()));
		messageSender.send(new ProducerRecord<>(topic().getName(), String.valueOf(LocalDateTime.now()).getBytes()));
		Thread.sleep(random.nextInt(300));
		logger.info("consumed={}", new String(record.value()));
	}

	@Override
	public TopicDefinition topic() {
		return TopicEnum.COFFEE_REQUEST.getTopic();
	}

}
