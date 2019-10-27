package com.mageddo.zipkin.customer.service;

import brave.Tracing;
import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final KafkaTemplate kafkaTemplate;

	public void orderAChair() {
		Tracing
		.currentTracer()
		.startScopedSpan("customer: chair ordering")
		;
		final var msg = "\ncustomer: I want a chair";
		log.info(msg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.STORE_CHAIR_DELIVERY_REQUEST,
			msg
		));
		Tracing.currentTracer().currentSpan().finish();
	}

	public void receiveChair(String msg) {
		final var customerMsg = new StringBuilder(msg)
		.append('\n')
		.append("customer: I have the chair, thanks!")
		.toString()
		;
		log.info(customerMsg);
	}
}
