package com.mageddo.micronaut.kafka.consumer;

import io.micronaut.context.ApplicationContext;

import javax.inject.Singleton;

@Singleton
public class ApplicationContextProvider {

	private static ApplicationContext context;

	public ApplicationContextProvider(final ApplicationContext context) {
		ApplicationContextProvider.context = context;
	}

	public static ApplicationContext context() {
		return context;
	}
}
