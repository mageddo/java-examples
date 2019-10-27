package com.mageddo.zipkin.chairfactory.lumberjack.service;

import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LumberJackService {

	private final KafkaTemplate kafkaTemplate;

	public void provideWood(String msg) {
		final var lumberJackMsg = new StringBuilder(msg)
		.append('\n')
		.append("lumberjack: The wood is ready, delivering to the factory")
		.toString()
		;
		log.info(lumberJackMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_WOOD_DELIVERED,
			lumberJackMsg
		));
	}
}
