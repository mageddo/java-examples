package com.mageddo.micronaut.kafka.consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public final class RetryUtils {

	private RetryUtils() {
	}

	public static RetryTemplate retryTemplate(
		final int maxAttempts, final Duration delay, final double multiplier,
		final Class<? extends Throwable>... recoverableExceptions
	) {
		final Map<Class<? extends Throwable>, Boolean> m = new HashMap<>();
		for (final Class<? extends Throwable> t : recoverableExceptions) {
			m.put(t, true);
		}
		if (recoverableExceptions.length == 0) {
			m.put(Exception.class, true);
		}
		final RetryTemplate template = new RetryTemplate();
		template.setRetryPolicy(new SimpleRetryPolicy(maxAttempts, m, true));
		final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(delay.toMillis());
		backOffPolicy.setMultiplier(multiplier);
		backOffPolicy.setMaxInterval((long) (delay.toMillis() * maxAttempts * multiplier));
		template.setBackOffPolicy(backOffPolicy);
		return template;
	}

	public static String format(RetryContext ctx) {
		return String.format(
			"retry=%s, lastError=%s",
			ctx.getRetryCount(),
			ctx.getLastThrowable() != null ? ExceptionUtils.getStackTrace(ctx.getLastThrowable()) : null
		);
	}

}
