package com.mageddo.kafka.jmx;

import com.mageddo.avro.User;
import com.mageddo.kafka.TopicEnum;
import com.mageddo.kafka.utils.AvroUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@ManagedResource
@Component
public class KafkaJMX {

	private final KafkaTemplate kafkaTemplate;

	public KafkaJMX(KafkaTemplate kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@ManagedOperation
	public String publishPingVanilla(){
		kafkaTemplate.send(new ProducerRecord(
			TopicEnum.Constants.PING_TOPIC,
			AvroUtils.serialize(createUser())
		));
		return "success";
	}

	@ManagedOperation
	public String publishPingAvroConfluentSerializer() throws ExecutionException, InterruptedException {
		kafkaTemplate.send(TopicEnum.Constants.PING_TOPIC, createUser()).get();
		return "success";
	}

	private static User createUser() {
		return User
			.newBuilder()
			.setName("Joao")
			.setFavoriteColor("Orange")
			.setFavoriteNumber(4)
			.build();
	}

	public static void main(String[] args) {
		AvroUtils.serialize(createUser());
	}

}
