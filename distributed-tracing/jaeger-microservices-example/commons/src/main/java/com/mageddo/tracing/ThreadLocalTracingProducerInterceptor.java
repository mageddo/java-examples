package com.mageddo.tracing;

import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.contrib.kafka.TracingProducerInterceptor;
import io.opentracing.util.GlobalTracer;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class ThreadLocalTracingProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

	private final TracingProducerInterceptor<K, V> delegate = new TracingProducerInterceptor<>();

	@Override
	public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
		final var resultRecord = delegate.onSend(record);
		Tracing.context(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()));
		return resultRecord;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
