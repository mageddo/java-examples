package com.mageddo.zipkin.chairfactory.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.service.ChairFactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryLumberJackWoodDeliveredMDB {

	private final ChairFactoryService chairFactoryService;

	@KafkaListener(topics = Topics.FACTORY_WOOD_DELIVERED)
	public void consume(String msg){
		chairFactoryService.requestChairMount(msg);
	}
}
