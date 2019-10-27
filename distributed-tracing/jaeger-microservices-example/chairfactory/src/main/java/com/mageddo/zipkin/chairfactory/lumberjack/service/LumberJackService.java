package com.mageddo.zipkin.chairfactory.lumberjack.service;

import com.mageddo.kafka.MessageSender;
import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LumberJackService {

	private final MessageSender kafkaTemplate;

	public void provideWood(String msg) {
		final var phrase = "lumberjack: The wood is ready, delivering to the factory";
		final var lumberJackMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_WOOD_DELIVERED,
			lumberJackMsg
		));
	}
}
