package com.mageddo.zipkin.chairfactory.lumberjack.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.lumberjack.service.LumberJackService;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LumberJackWoodDeliveryRequestMDB {

	private final LumberJackService lumberJackService;

	@KafkaListener(topics = Topics.FACTORY_LUMBERJACK_WOOD_DELIVERY_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
		.buildSpan("lumberjack: deliverying wood to the factory")
		.asChildOf(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()))
		.withTag("msg", record.value())
		.start()
		;
		GlobalTracer.get().activateSpan(span);
		lumberJackService.provideWood(record.value());
		span.finish();
	}
}
