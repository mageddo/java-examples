package com.mageddo.zipkin.customer.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.customer.service.CustomerService;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerChairDeliveryMDB {

	private final CustomerService customerService;

	@KafkaListener(topics = Topics.CUSTOMER_CHAIR_DELIVERY)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
			.buildSpan("customer: chair received")
			.withTag("msg", record.value())
			.start();

		customerService.receiveChair(record.value());

		span.finish();
	}

}
