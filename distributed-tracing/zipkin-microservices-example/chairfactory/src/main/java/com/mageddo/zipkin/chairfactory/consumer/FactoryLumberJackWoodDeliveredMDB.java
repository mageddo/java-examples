package com.mageddo.zipkin.chairfactory.consumer;

import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.service.ChairFactoryService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactoryLumberJackWoodDeliveredMDB {

	private final ChairFactoryService chairFactoryService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.FACTORY_WOOD_DELIVERED)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
			.name("factory: requesting chair mount")
			.tag("msg", record.value())
			.start()
		;
		chairFactoryService.requestChairMount(record.value());
		span.finish();
	}
}
