package com.mageddo.zipkin;

import com.mageddo.zipkin.customer.service.CustomerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;

@EnableKafka
@SpringBootApplication
public class App implements InitializingBean {

	private final boolean createTopics;
	private final CustomerService customerService;
	private final KafkaTemplate kafkaTemplate;

	public App(@Value("${create.topics:false}") boolean createTopics, CustomerService customerService, KafkaTemplate kafkaTemplate) {
		this.createTopics = createTopics;
		this.customerService = customerService;
		this.kafkaTemplate = kafkaTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if(this.createTopics){
			createTopics();
			return;
		}

		customerService.orderAChair();
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
