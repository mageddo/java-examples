package com.mageddo.tracing;

import io.opentracing.contrib.kafka.TracingConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class ThreadLocalTracingConsumerInterceptor<K, V> implements ConsumerInterceptor<K, V> {

	private final TracingConsumerInterceptor<K, V> delegate = new TracingConsumerInterceptor<>();

	@Override
	public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
		final var result = delegate.onConsume(records);
//		TracingKafkaUtils.extractSpanContext(records.j)
		return result;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {

	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
