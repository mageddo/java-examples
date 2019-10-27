package com.mageddo.zipkin.store.consumer;

import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreChairDeliveredMDB {

	private final StoreService storeService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERED)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
			.name("store: chair delivery to customer")
			.tag("msg", record.value())
			.start()
		;
		storeService.deliverChairToCustomer(record.value());
		span.finish();
	}
}
