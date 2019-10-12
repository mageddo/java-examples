package com.mageddo.micronaut.kafka.mdb;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
@KafkaListener(clientId = "vanilla")
public class PongMDB {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Topic("ping")
	public void receive(ConsumerRecord<String, byte[]> record) {
		logger.info(
			"status=ping, key={}, partition={}, offset={}, record={}",
			record.key(), record.partition(), record.offset(), new String(record.value())
		);
	}
}
