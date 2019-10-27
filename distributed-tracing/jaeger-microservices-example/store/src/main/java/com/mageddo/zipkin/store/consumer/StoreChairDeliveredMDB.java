package com.mageddo.zipkin.store.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.store.service.StoreService;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreChairDeliveredMDB {

	private final StoreService storeService;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERED)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
			.buildSpan("store: chair delivery to customer")
			.withTag("msg", record.value())
			.start()
		;
		storeService.deliverChairToCustomer(record.value());
		span.finish();
	}
}
