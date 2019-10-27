package com.mageddo.zipkin.chairfactory.lumberjack.consumer;

import com.mageddo.zipkin.chairfactory.lumberjack.service.LumberJackService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LumberJackWoodDeliveryRequestMDB {

	private final LumberJackService lumberJackService;

	@KafkaListener
	public void consume(String msg){
		lumberJackService.provideWood(msg);
	}
}
