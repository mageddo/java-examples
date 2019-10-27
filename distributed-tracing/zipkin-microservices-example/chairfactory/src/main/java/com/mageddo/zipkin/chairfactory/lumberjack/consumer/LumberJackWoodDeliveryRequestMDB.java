package com.mageddo.zipkin.chairfactory.lumberjack.consumer;

import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.lumberjack.service.LumberJackService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LumberJackWoodDeliveryRequestMDB {

	private final LumberJackService lumberJackService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.FACTORY_LUMBERJACK_WOOD_DELIVERY_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
		.name("lumberjack: deliverying wood to the factory")
		.tag("msg", record.value())
		;
		lumberJackService.provideWood(record.value());
		span.finish();
	}
}
