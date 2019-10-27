package com.mageddo.zipkin;

import brave.Tracing;
import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.tracing.KafkaTracingConsumerFactory;
import com.mageddo.zipkin.tracing.KafkaTracingProducerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.concurrent.TimeUnit;

@EnableKafka
@SpringBootApplication
public class CustomerStarter {

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
//			.traceId128Bit(true)
			.localServiceName("chair-land")
			.alwaysReportSpans()
			.spanReporter(spanReporter)
			.build();

		SpringApplication.run(CustomerStarter.class, args);

	}


	@Bean
	public KafkaTracing createKafkaTracing() {
		return KafkaTracing.newBuilder(Tracing.current())
			.writeB3SingleFormat(true) // for more efficient propagation
			.remoteServiceName("kafka")
			.build();
	}

	@Bean
	public ConsumerFactory<?, ?> defaultKafkaConsumerFactory(
		KafkaTracing kafkaTracing, KafkaProperties kafkaProperties
	) {
		return new KafkaTracingConsumerFactory<>(
			new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties()),
			kafkaTracing
		);
	}

	@Bean
	public ProducerFactory<?, ?> kafkaProducerFactory(
		KafkaTracing kafkaTracing, KafkaProperties kafkaProperties
	) {
		DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
			kafkaProperties.buildProducerProperties());
		String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
		if (transactionIdPrefix != null) {
			factory.setTransactionIdPrefix(transactionIdPrefix);
		}
		return new KafkaTracingProducerFactory<>(factory, kafkaTracing);
	}

}
