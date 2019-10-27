package com.mageddo.zipkin.store.consumer;

import brave.Tracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreChairDeliveredMDB {

	private final StoreService storeService;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERED)
	public void consume(String msg){
		Tracing
			.currentTracer()
			.startScopedSpan("store: chair delivery to customer")
			.tag("msg", msg)
		;
		storeService.deliverChairToCustomer(msg);
		Tracing.currentTracer().currentSpan().finish();
	}
}
