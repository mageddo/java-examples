package com.mageddo.micronaut.kafka.consumer;

public interface TopicDefinition {
	String name();
	String dlqName();
}
