package com.mageddo.kafka.jmx;

import com.mageddo.kafka.TopicEnum;
import com.mageddo.kafka.producer.MessageSender;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@ManagedResource
@Component
public class KafkaJMX {

	private final MessageSender messageSender;

	public KafkaJMX(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@ManagedOperation
	public String postMessages(int quantity, String topic){
		for (int i = 0; i < quantity; i++) {
			messageSender.send(topic, DigestUtils.md5DigestAsHex(String.valueOf(Math.random()).getBytes()));
		}
		return "success";
	}

	@ManagedOperation
	public String makeCoffee(){
		messageSender.send(new ProducerRecord<>(
			TopicEnum.COFFEE_REQUEST.getTopic().getName(),
			String.valueOf(LocalDateTime.now()).getBytes()
		));
		return "success";
	}

}
