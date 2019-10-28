package com.mageddo.zipkin.chairfactory.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.service.ChairFactoryService;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
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
		final var span = GlobalTracer.get()
			.buildSpan("factory: starting construction process")
			.asChildOf(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()))
			.withTag("msg", record.value())
			.start()
		;
		GlobalTracer.get().activateSpan(span);
		chairFactoryService.startChairConstruction(record.value());
		span.finish();
	}
}
