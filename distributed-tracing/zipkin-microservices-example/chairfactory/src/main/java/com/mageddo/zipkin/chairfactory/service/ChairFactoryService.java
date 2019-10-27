package com.mageddo.zipkin.chairfactory.service;

import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChairFactoryService {

	private final KafkaTemplate kafkaTemplate;

	public void startChairConstruction(String msg) {
		final var factoryMsg = new StringBuilder(msg)
		.append('\n')
		.append("factory: We are starting the chair construction process")
		.toString()
		;
		log.info(factoryMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_LUMBERJACK_WOOD_DELIVERY_REQUEST,
			factoryMsg
		));
	}

	public void requestChairMount(String msg) {
		final var factoryMsg = new StringBuilder(msg)
			.append('\n')
			.append("factory: We have the wood, requesting chair mount to locksmith")
			.toString()
			;
		log.info(factoryMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_LOCKSMITH_CHAIR_MOUNT_REQUEST,
			factoryMsg
		));
	}

	public void requestChairPaint(String msg) {
		final var factoryMsg = new StringBuilder(msg)
		.append('\n')
		.append("factory: We have the mounted chair, request the painting")
		.toString()
		;
		log.info(factoryMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_PAINTER_CHAIR_PAINT_REQUEST,
			factoryMsg
		));
	}

	public void deliveryChairToStore(String msg) {
		final var factoryMsg = new StringBuilder(msg)
		.append('\n')
		.append("factory: Chair is ready, delivering to the store")
		.toString()
		;
		log.info(factoryMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.STORE_CHAIR_DELIVERED,
			factoryMsg
		));
	}
}
