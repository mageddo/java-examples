package com.mageddo.zipkin.store.service;

import com.mageddo.zipkin.Topics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

	private final KafkaTemplate kafkaTemplate;

	public void requestChairToTheFactory(String msg){
		final var storeMsg = new StringBuilder(msg)
			.append('\n')
			.append("store: I'm ordering the chair to the factory")
			.toString()
			;
		log.info(storeMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.FACTORY_CHAIR_DELIVERY_REQUEST,
			storeMsg
		));
	}

	public void deliverChairToCustomer(String msg) {
		final var storeMsg = new StringBuilder(msg)
			.append('\n')
			.append("store: We have the chair, delivering to the customer")
			.toString()
			;
		log.info(storeMsg);
		kafkaTemplate.send(new ProducerRecord<>(
			Topics.CUSTOMER_CHAIR_DELIVERY,
			storeMsg
		));
	}
}
