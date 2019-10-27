package com.mageddo.zipkin.chairfactory.locksmith.service;

import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockSmithService {

	private final KafkaTemplate kafkaTemplate;

	public void mountChair(String msg){
		final var phrase = "locksmith: the chair is mounted, delivering to the factory";
		final var locksmithMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_MOUNTED_CHAIR_DELIVERY,
			locksmithMsg
		));
	}
}
