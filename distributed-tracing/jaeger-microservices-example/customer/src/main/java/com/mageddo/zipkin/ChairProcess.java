package com.mageddo.zipkin;

import com.mageddo.zipkin.customer.service.CustomerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class ChairProcess implements InitializingBean {

	private final boolean createTopics;
	private final KafkaTemplate kafkaTemplate;
	private final CustomerService customerService;

	public ChairProcess(
		@Value("${create.topics:false}") boolean createTopics, KafkaTemplate kafkaTemplate, CustomerService customerService
	) {
		this.createTopics = createTopics;
		this.kafkaTemplate = kafkaTemplate;
		this.customerService = customerService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if (this.createTopics) {
			createTopics();
			return;
		}
//		customerService.orderAChair();
	}

	private void createTopics() throws IllegalAccessException, ExecutionException, InterruptedException {
		for (final var topic : Topics.class.getDeclaredFields()) {
			kafkaTemplate.send(new ProducerRecord<>(
				topic.get(null).toString(), "creating topic"
			)).get();
		}
		System.exit(0);
	}
}
