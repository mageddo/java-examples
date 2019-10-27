package com.mageddo.zipkin.customer.consumer;

import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerChairDeliveryMDB {

	private final CustomerService customerService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.CUSTOMER_CHAIR_DELIVERY)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
			.name("customer: chair received")
			.tag("msg", record.value())
			.start();

		customerService.receiveChair(record.value());

		span.finish();
	}

}
