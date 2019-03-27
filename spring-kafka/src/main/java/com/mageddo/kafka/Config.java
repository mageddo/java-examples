package com.mageddo.kafka;

import com.mageddo.kafka.consumer.ConsumerDeclarer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class Config extends ConsumerDeclarer implements InitializingBean {

	public Config(
		ConfigurableBeanFactory beanFactory,
		KafkaProperties kafkaProperties,
		@Value("${spring.kafka.consumer.autostartup:true}") boolean autostartup
	) {
		super(beanFactory, kafkaProperties, autostartup);
	}

	@Override
	public void afterPropertiesSet() {
		this.declare(
			Stream.of(TopicEnum.values())
				.map(TopicEnum::getTopic)
				.collect(Collectors.toList())
		);
	}

}
