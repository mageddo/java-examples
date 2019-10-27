package com.mageddo.zipkin;

import com.mageddo.zipkin.customer.service.CustomerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args)
		.getBean(CustomerService.class)
		.orderAChair();
	}
}
