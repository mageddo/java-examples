package com.mageddo.kafka.consumer;

import com.mageddo.avro.User;
import com.mageddo.kafka.TopicConsumer;
import com.mageddo.kafka.TopicDefinition;
import com.mageddo.kafka.TopicEnum;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.mageddo.kafka.TopicEnum.Constants.PING_FACTORY;

@Component
public class PingConsumer implements TopicConsumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@KafkaListener(topics = "#{__listener.topic().getName()}", containerFactory = PING_FACTORY)
	public void consume(ConsumerRecord<?, User> record) {
		logger.info("consumed={}", record.value());
	}

	@Override
	public TopicDefinition topic() {
		return TopicEnum.PING.getTopic();
	}

}
