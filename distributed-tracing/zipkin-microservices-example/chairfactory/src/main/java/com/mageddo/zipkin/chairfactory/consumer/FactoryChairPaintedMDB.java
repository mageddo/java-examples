package com.mageddo.zipkin.chairfactory.consumer;

import brave.Tracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.service.ChairFactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryChairPaintedMDB {

	private final ChairFactoryService chairFactoryService;

	@KafkaListener(topics = Topics.FACTORY_CHAIR_PAINTED)
	public void consume(String msg){
		Tracing
			.currentTracer()
			.startScopedSpan("factory: chair delivery to store")
			.tag("msg", msg)
		;
		chairFactoryService.deliveryChairToStore(msg);
		Tracing.currentTracer().currentSpan().finish();
	}
}
