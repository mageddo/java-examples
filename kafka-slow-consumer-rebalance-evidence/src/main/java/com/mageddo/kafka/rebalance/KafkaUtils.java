package com.mageddo.kafka.rebalance;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public final class KafkaUtils {

	private KafkaUtils() {
	}

	public static Map<String, Object> getDefaultProps(){
		final var props = new LinkedHashMap<String, Object>();
		props.put("bootstrap.servers", "localhost:9092,kafka.intranet:9092");
		props.put("enable.auto.commit", "false");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30_000);
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 960_000);
		return props;
	}

	public static void sleep(Duration d){
		try {
			Thread.sleep(d.toMillis());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
