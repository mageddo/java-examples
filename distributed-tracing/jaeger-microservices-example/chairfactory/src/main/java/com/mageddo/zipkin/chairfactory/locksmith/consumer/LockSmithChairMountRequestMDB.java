package com.mageddo.zipkin.chairfactory.locksmith.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.locksmith.service.LockSmithService;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockSmithChairMountRequestMDB {

	private final LockSmithService lockSmithService;

	@KafkaListener(topics = Topics.FACTORY_LOCKSMITH_CHAIR_MOUNT_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
		.buildSpan("factory: chair mount")
		.asChildOf(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()))
		.withTag("msg", record.value())
		.start()
		;
		lockSmithService.mountChair(record.value());
		span.finish();
	}
}
