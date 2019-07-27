package com.mageddo.kafka;

import java.time.Duration;

public enum TopicEnum {

	PING(new Topic(Constants.PING_TOPIC)
		.autoConfigure(true)
		.factory(Constants.PING_FACTORY)
		.consumers(1)
		.interval(Duration.ofSeconds(5))
		.maxTries(2)
	),

	;

	private final Topic topic;

	TopicEnum(Topic topic) {
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}

	public static class Constants {
		public static final String PING_FACTORY = "PING_FACTORY";
		public static final String PING_TOPIC = "PING";
	}

}
