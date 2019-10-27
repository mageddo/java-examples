package com.mageddo.zipkin.store.consumer;

import brave.Tracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreChairDeliveryRequestMDB {

	private final StoreService storeService;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERY_REQUEST)
	public void consume(String msg){
		Tracing
			.currentTracer()
			.startScopedSpan("store ordering chair to the factory")
			.tag("msg", msg)
		;
		storeService.requestChairToTheFactory(msg);
		Tracing.currentTracer().currentSpan().finish();
	}
}
