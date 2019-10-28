package com.mageddo.zipkin.store.consumer;

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
public class StoreChairDeliveredMDB {

	private final StoreService storeService;

	@KafkaListener(topics = Topics.STORE_CHAIR_DELIVERED)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
			.buildSpan("store: chair delivery to customer")
			.asChildOf(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()))
			.withTag("msg", record.value())
			.start()
		;
		try(var scope = GlobalTracer.get().activateSpan(span)) {
			storeService.deliverChairToCustomer(record.value());
			span.finish();
		}
	}
}
