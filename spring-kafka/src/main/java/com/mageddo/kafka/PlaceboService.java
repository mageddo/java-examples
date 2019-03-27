package com.mageddo.kafka;

import com.mageddo.kafka.producer.MessageSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PlaceboService {

	private final MessageSender messageSender;

	public PlaceboService(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@Transactional
	public void doSomethingImportant(){
		messageSender.send("TOPIC_TEST", String.valueOf(UUID.randomUUID()));
	}
}
