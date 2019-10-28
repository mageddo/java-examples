package com.mageddo.zipkin.customer.service;

import com.mageddo.kafka.MessageSender;
import com.mageddo.zipkin.Topics;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final MessageSender kafkaTemplate;

	public void orderAChair() {
		final var span = GlobalTracer.get()
			.buildSpan("customer: chair ordering")
			.start();
		GlobalTracer.get().activateSpan(span);
		final var msg = "\ncustomer: I want a chair";
		log.info(msg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.STORE_CHAIR_DELIVERY_REQUEST,
			msg
		));
		span.finish();
	}

	public void receiveChair(String msg) {
		final var phrase = "customer: I have the chair, thanks!";
		final var customerMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
	}
}
