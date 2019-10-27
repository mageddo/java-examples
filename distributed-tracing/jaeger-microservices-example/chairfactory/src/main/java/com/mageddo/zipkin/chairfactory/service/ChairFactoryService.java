package com.mageddo.zipkin.chairfactory.service;

import com.mageddo.kafka.MessageSender;
import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChairFactoryService {

	private final MessageSender kafkaTemplate;

	public void startChairConstruction(String msg) {
		final var phrase = "factory: We are starting the chair construction process";
		final var factoryMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_LUMBERJACK_WOOD_DELIVERY_REQUEST,
			factoryMsg
		));
	}

	public void requestChairMount(String msg) {
		final var phrase = "factory: We have the wood, requesting chair mount to locksmith";
		final var factoryMsg = new StringBuilder(msg)
			.append('\n')
			.append(phrase)
			.toString()
			;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_LOCKSMITH_CHAIR_MOUNT_REQUEST,
			factoryMsg
		));
	}

	public void requestChairPaint(String msg) {
		final var phrase = "factory: We have the mounted chair, request the painting";
		final var factoryMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_PAINTER_CHAIR_PAINT_REQUEST,
			factoryMsg
		));
	}

	public void deliveryChairToStore(String msg) {
		final var phrase = "factory: Chair is ready, delivering to the store";
		final var factoryMsg = new StringBuilder(msg)
		.append('\n')
		.append(phrase)
		.toString()
		;
		log.info(phrase);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.STORE_CHAIR_DELIVERED,
			factoryMsg
		));
	}
}
