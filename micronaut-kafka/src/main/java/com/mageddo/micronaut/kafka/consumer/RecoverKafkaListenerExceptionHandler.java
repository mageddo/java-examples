package com.mageddo.micronaut.kafka.consumer;

import com.mageddo.micronaut.kafka.KafkaProducer;
import io.micronaut.configuration.kafka.exceptions.KafkaListenerException;
import io.micronaut.configuration.kafka.exceptions.KafkaListenerExceptionHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

import static com.mageddo.micronaut.kafka.consumer.ApplicationContextProvider.context;

public interface RecoverKafkaListenerExceptionHandler extends KafkaListenerExceptionHandler {

	Logger LOG = LoggerFactory.getLogger("RecoverKafkaListenerExceptionHandler");

	default void handle(KafkaListenerException exception){
		if(exception.getConsumerRecord().isPresent()){
			final ConsumerRecord<String, byte[]> consumerRecord = (ConsumerRecord<String, byte[]>) exception
				.getConsumerRecord()
				.get()
			;
			try {
				final Consumer c = (Consumer) exception.getKafkaListener();
				context()
				.getBean(KafkaProducer.class)
				.send(new ProducerRecord<>(c.topic().dlqName(), consumerRecord.key(), consumerRecord.value()))
				.get()
				;
				LOG.warn(
					"status=recovering, partition={}, offset={}, key={}, value={}",
					consumerRecord.partition(), consumerRecord.offset(),
					consumerRecord.key(), new String(consumerRecord.value()), exception
				);
			} catch (InterruptedException | ExecutionException e) {
				LOG.warn(
					"status=cant-send-to-dlq, partition={}, offset={}, key={}, value={}",
					consumerRecord.partition(), consumerRecord.offset(),
					consumerRecord.key(), new String(consumerRecord.value()), exception
				);
				throw new RuntimeException(e);
			}
		}
	}
}
