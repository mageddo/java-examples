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
public class FactoryMountedChairMDB {

	private final ChairFactoryService chairFactoryService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.FACTORY_MOUNTED_CHAIR_DELIVERY)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
			.name("factory: painting request")
			.tag("msg", record.value())
			.start()
		;
		chairFactoryService.requestChairPaint(record.value());
		span.finish();
	}
}
