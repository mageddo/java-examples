package com.mageddo.kafka.rebalance.consumer;

import com.mageddo.common.retry.RetryUtils;
import com.mageddo.kafka.rebalance.KafkaUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

public class SlowConsumer implements Consumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final KafkaProducer<String, String> producer;
	private final String consumerId;

	public SlowConsumer(KafkaProducer<String, String> producer, String consumerId) {
		this.producer = producer;
		this.consumerId = consumerId;
	}

	public void setup() {
		final var props = KafkaUtils.getDefaultProps();
		props.put("group.id", "app_group_id");

		final var consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Collections.singletonList("fruit_topic"));

		while (true) {
			logger.info("status=polling, consumer={}", this.consumerId);
			final var records = consumer.poll(Duration.ofSeconds(1));
			RetryUtils.retryTemplate(3, Duration.ofMinutes(5), 1.0).execute(ctx -> {
				for (final var record : records) {
					logger.info("status=processing, consumer={}, record={}, size={}", this.consumerId, record.value(), records.count());
				}
				producer.send(new ProducerRecord<>("fruit_topic", UUID.randomUUID().toString()));
//				if(records.count() > 0)
//					throw new UnsupportedOperationException();
				KafkaUtils.sleep(Duration.ofSeconds(3));
				return  null;
			}, (ctx) -> {
				logger.info("status=recovering, consumer={}", this.consumerId);
				return null;
			});
			consumer.commitSync();
		}
	}
}
