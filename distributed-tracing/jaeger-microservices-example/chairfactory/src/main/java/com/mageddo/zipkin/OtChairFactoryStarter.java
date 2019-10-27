package com.mageddo.zipkin;

import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class OtChairFactoryStarter {

	public static void main(String[] args) {
		GlobalTracer.registerIfAbsent(Tracing.createTracer("chairland"));
		SpringApplication.run(OtChairFactoryStarter.class, args);
	}

}
