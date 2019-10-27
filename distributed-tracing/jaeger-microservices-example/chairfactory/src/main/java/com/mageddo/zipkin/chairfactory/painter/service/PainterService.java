package com.mageddo.zipkin.chairfactory.painter.service;

import com.mageddo.kafka.MessageSender;
import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PainterService {

	private final MessageSender kafkaTemplate;

	public void paintChair(String msg) {
		final var phrase = "painter: The chair is painted, delivering to the factory";
		final var painterMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_CHAIR_PAINTED,
			painterMsg
		));
	}
}
