package com.mageddo.zipkin.chairfactory.locksmith.consumer;

import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.locksmith.service.LockSmithService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockSmithChairMountRequestMDB {

	private final LockSmithService lockSmithService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.FACTORY_LOCKSMITH_CHAIR_MOUNT_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
		.name("factory: chair mount")
		.tag("msg", record.value())
		.start()
		;
		lockSmithService.mountChair(record.value());
		span.finish();
	}
}
