package com.mageddo.zipkin;

import brave.Tracing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.concurrent.TimeUnit;

@EnableKafka
@SpringBootApplication
public class ChairFactoryStarter {

	public static void main(String[] args) {

		var sender = OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans")
			.toBuilder()
			.connectTimeout(10000)
			.readTimeout(10000)
			.writeTimeout(10000)
			.build();
		var spanReporter = AsyncReporter
			.builder(sender)
			.closeTimeout(5, TimeUnit.SECONDS)
			.build();

		// Create a tracing component with the service name you want to see in Zipkin.
		Tracing.newBuilder()
			.traceId128Bit(true)
			.localServiceName("chairland")
			.spanReporter(spanReporter)
			.build();

		SpringApplication.run(ChairFactoryStarter.class, args);

	}

}
