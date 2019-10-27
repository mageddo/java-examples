package com.mageddo.zipkin.store.consumer;

import com.mageddo.tracing.Tracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.store.service.StoreService;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreChairDeliveryRequestMDB {

	private final StoreService storeService;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERY_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		Tracing.context(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()));
		final var span = GlobalTracer.get()
			.buildSpan("store: ordering chair to the factory")
			.asChildOf(Tracing.context())
			.withTag("msg", record.value())
			.start()
		;
		storeService.requestChairToTheFactory(record.value());
		span.finish();
	}
}
