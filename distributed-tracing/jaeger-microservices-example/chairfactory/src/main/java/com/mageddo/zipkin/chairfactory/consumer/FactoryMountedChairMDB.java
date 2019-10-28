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
public class FactoryMountedChairMDB {

	private final ChairFactoryService chairFactoryService;

	@KafkaListener(topics = Topics.FACTORY_MOUNTED_CHAIR_DELIVERY)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
			.buildSpan("factory: painting request")
			.asChildOf(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()))
			.withTag("msg", record.value())
			.start()
		;
		try(var scope = GlobalTracer.get().activateSpan(span)) {
			chairFactoryService.requestChairPaint(record.value());
		} finally {
			span.finish();
		}
	}
}
