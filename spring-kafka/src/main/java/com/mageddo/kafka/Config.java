package com.mageddo.kafka;

import com.mageddo.kafka.consumer.ConsumerDeclarer;
import com.mageddo.kafka.producer.MessageSender;
import com.mageddo.kafka.producer.MessageSenderImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config implements InitializingBean {

	@Autowired
	private ConsumerDeclarer consumerDeclarer;

	@Override
	public void afterPropertiesSet() {
		consumerDeclarer.declare(
			Stream.of(TopicEnum.values())
				.map(TopicEnum::getTopic)
				.collect(Collectors.toList())
		);
	}

	@Bean
	public ConsumerDeclarer consumerDeclarer(
		ConfigurableBeanFactory beanFactory,
		KafkaProperties kafkaProperties,
		@Value("${spring.kafka.consumer.autostartup:true}") boolean autostartup /* startup consumer automatically */
	) {
		return new ConsumerDeclarer(beanFactory, kafkaProperties, autostartup);
	}

	@Bean
	public MessageSender messageSender(KafkaTemplate<String, byte[]> template) {
		return new MessageSenderImpl(template);
	}
}
