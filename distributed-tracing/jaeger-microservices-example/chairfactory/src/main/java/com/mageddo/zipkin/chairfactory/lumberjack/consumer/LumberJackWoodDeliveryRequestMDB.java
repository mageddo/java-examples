package com.mageddo.zipkin.chairfactory.lumberjack.consumer;

import brave.Tracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.lumberjack.service.LumberJackService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LumberJackWoodDeliveryRequestMDB {

	private final LumberJackService lumberJackService;

	@KafkaListener(topics = Topics.FACTORY_LUMBERJACK_WOOD_DELIVERY_REQUEST)
	public void consume(String msg){
		Tracing
			.currentTracer()
			.startScopedSpan("lumberjack: deliverying wood to the factory")
			.tag("msg", msg)
		;
		lumberJackService.provideWood(msg);
		Tracing.currentTracer().currentSpan().finish();
	}
}
