package com.mageddo.kafka.rebalance;

import com.mageddo.kafka.rebalance.consumer.FastConsumer;
import com.mageddo.kafka.rebalance.consumer.SlowConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
	public static void main(String[] args) throws Exception {

		final var producer = new KafkaProducer<String, String>(KafkaUtils.getDefaultProps());
		producer.send(new ProducerRecord<>("fruit_topic", "start message"));
		producer.send(new ProducerRecord<>("car_topic", "start message"));

		final var consumers = Arrays.asList(
			new SlowConsumer(producer, "slow-1"),
			new SlowConsumer(producer, "slow-2"),
			new FastConsumer(producer,"fast-1"),
			new FastConsumer(producer,"fast-2")
		);
		final var executorService = Executors.newFixedThreadPool(10);
		for (final var consumer : consumers) {
			executorService.submit(consumer::setup);
		}
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	}
}
