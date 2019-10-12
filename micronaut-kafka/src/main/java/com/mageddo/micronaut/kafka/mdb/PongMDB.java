package com.mageddo.micronaut.kafka.mdb;

import com.mageddo.common.retry.RetryUtils;
import com.mageddo.micronaut.kafka.KafkaProducer;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetStrategy;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.configuration.kafka.exceptions.KafkaListenerException;
import io.micronaut.configuration.kafka.exceptions.KafkaListenerExceptionHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.Duration;
import java.util.Random;

@Singleton
@KafkaListener(groupId = "pongGroupId", clientId = "vanilla", offsetStrategy = OffsetStrategy.SYNC)
public class PongMDB implements KafkaListenerExceptionHandler  {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final KafkaProducer kafkaProducer;

	public PongMDB(KafkaProducer kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}

	@Topic("ping")
	public void receive(ConsumerRecord<String, byte[]> record) {
		logger.info(
			"status=pong, key={}, partition={}, offset={}, record={}",
			record.key(), record.partition(), record.offset(), new String(record.value())
		);
		if(new Random().nextInt(10) == 1){
			throw new RuntimeException("Failed to consume: " + new String(record.value()));
		}
	}

	@Override
	public void handle(KafkaListenerException exception) {
		final var consumerRecord = (ConsumerRecord<String, byte[]>) exception.getConsumerRecord().get();
		RetryUtils
		.retryTemplate(3, Duration.ofSeconds(2), 1.5, Exception.class)
		.execute(ctx -> {
			logger.warn("status=retry");
			this.receive(consumerRecord);
			return null;
		}, ctx -> {
			logger.warn(
				"status=recovering, partition={}, offset={}, key={}, value={}",
				consumerRecord.partition(), consumerRecord.offset(),
				consumerRecord.key(), new String(consumerRecord.value()), exception
			);
			kafkaProducer
			.send(new ProducerRecord<>(dlqName(), consumerRecord.key(), consumerRecord.value()))
			.get()
			;
			return null;
		});
	}

	private String dlqName() {
		return "ping_dlq";
	}
}
