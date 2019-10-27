package com.mageddo.zipkin.chairfactory.painter.service;

import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PainterService {

	private final KafkaTemplate kafkaTemplate;

	public void paintChair(String msg) {
		final var painterMsg = new StringBuilder(msg)
		.append('\n')
		.append("painter: The chair is painted, delivering to the factory")
		.toString()
		;
		log.info(painterMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_CHAIR_PAINTED,
			painterMsg
		));
	}
}
