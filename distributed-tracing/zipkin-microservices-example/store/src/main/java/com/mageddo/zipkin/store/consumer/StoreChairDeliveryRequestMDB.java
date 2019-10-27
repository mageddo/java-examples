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
public class StoreChairDeliveryRequestMDB {

	private final StoreService storeService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERY_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
			.name("store: ordering chair to the factory")
			.tag("msg", record.value())
			.start()
		;
		storeService.requestChairToTheFactory(record.value());
		span.finish();
	}
}
