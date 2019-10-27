package com.mageddo.zipkin.customer.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerChairDeliveryMDB {

	private final CustomerService customerService;

	@KafkaListener(topics = Topics.CUSTOMER_CHAIR_DELIVERY)
	public void consume(String msg){
		customerService.receiveChair(msg);
	}

}
