package com.mageddo.kafka;

import java.time.Duration;

public enum TopicEnum {

	COFFEE_REQUEST(new Topic("COFFEE_REQUEST")
		.autoConfigure(true)
		.factory(Constants.COFFEE_REQUEST_FACTORY)
		.consumers(5)
		.interval(Duration.ofSeconds(5))
		.maxTries(2)
	),

	TRANSACTION_CHECKOUT(new Topic("TRANSACTION_CHECKOUT")
		.autoConfigure(true)
		.factory(Constants.TRANSACTION_CHECKOUT_FACTORY)
		.consumers(5)
		.interval(Duration.ofSeconds(5))
		.maxTries(2)
	);

	private final Topic topic;

	TopicEnum(Topic topic) {
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}

	public static class Constants {
		public static final String COFFEE_REQUEST_FACTORY = "COFFEE_REQUEST_FACTORY";
		public static final String TRANSACTION_CHECKOUT_FACTORY = "TRANSACTION_CHECKOUT_FACTORY";
	}

}
