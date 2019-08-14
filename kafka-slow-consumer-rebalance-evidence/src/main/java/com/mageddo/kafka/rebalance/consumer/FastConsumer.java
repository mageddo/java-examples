package com.mageddo.kafka.rebalance.consumer;

import com.mageddo.kafka.rebalance.KafkaUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class FastConsumer implements Consumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final KafkaProducer<String, String> producer;
	private final String consumerId;

	public FastConsumer(KafkaProducer<String, String> producer, String consumerId) {
		this.producer = producer;
		this.consumerId = consumerId;
	}

	public void setup() {
		final var props = KafkaUtils.getDefaultProps();
		props.put("group.id", "app_group_id");

		final var consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Collections.singletonList("car_topic"));

		while (true) {
			logger.info("status=polling, consumer={}", this.consumerId);
			final var records = consumer.poll(Duration.ofSeconds(2));
			for (final var record : records) {
				logger.info("status=processing, consumer={}, record={}, size={}", this.consumerId, record.value(), records.count());
			}
			KafkaUtils.sleep(Duration.ofSeconds(3));
			producer.send(new ProducerRecord<>("car_topic", UUID.randomUUID().toString()));
			consumer.commitSync();
		}
	}
}
