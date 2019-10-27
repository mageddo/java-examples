package com.mageddo.zipkin.tracing;

import brave.kafka.clients.KafkaTracing;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.kafka.core.ProducerFactory;

@RequiredArgsConstructor
public class KafkaTracingProducerFactory<K, V> implements ProducerFactory<K, V> {

	private final ProducerFactory delegate;
	private final KafkaTracing kafkaTracing;

	@Override
	public Producer<K, V> createProducer() {
		return kafkaTracing.producer(delegate.createProducer());
	}

	@Override
	public Producer<K, V> createProducer(String txIdPrefix) {
		return kafkaTracing.producer(delegate.createProducer(txIdPrefix));
	}

	@Override
	public boolean transactionCapable() {
		return delegate.transactionCapable();
	}

	@Override
	public void closeProducerFor(String transactionIdSuffix) {
		delegate.closeProducerFor(transactionIdSuffix);
	}

	@Override
	public boolean isProducerPerConsumerPartition() {
		return delegate.isProducerPerConsumerPartition();
	}

	@Override
	public void closeThreadBoundProducer() {
		delegate.closeThreadBoundProducer();
	}
}
