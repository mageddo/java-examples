package com.mageddo.zipkin.chairfactory.consumer;

import brave.Tracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.service.ChairFactoryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryChairDeliveryMDB {

	private final ChairFactoryService chairFactoryService;

	@KafkaListener(topics = Topics.FACTORY_CHAIR_DELIVERY_REQUEST)
	public void consume(ConsumerRecord<String, String> record) {
		Tracing
			.currentTracer()
			.startScopedSpan("factory: starting construction process")
			.tag("msg", record.value())
		;
		chairFactoryService.startChairConstruction(record.value());
		Tracing.currentTracer().currentSpan().finish();
	}
}
