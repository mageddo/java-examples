package com.mageddo.zipkin.tracing;

import brave.kafka.clients.KafkaTracing;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@RequiredArgsConstructor
public class KafkaTracingConsumerFactory<K, V> implements ConsumerFactory<K, V> {

	private final DefaultKafkaConsumerFactory<K, V> delegate;
	private final KafkaTracing kafkaTracing;

	@Override
	public Consumer<K, V> createConsumer(String groupId, String clientIdPrefix, String clientIdSuffix) {
		return kafkaTracing.consumer(delegate.createConsumer(groupId, clientIdPrefix, clientIdSuffix));
	}

	@Override
	public boolean isAutoCommit() {
		return delegate.isAutoCommit();
	}

	@Override
	public Map<String, Object> getConfigurationProperties() {
		return delegate.getConfigurationProperties();
	}
}
