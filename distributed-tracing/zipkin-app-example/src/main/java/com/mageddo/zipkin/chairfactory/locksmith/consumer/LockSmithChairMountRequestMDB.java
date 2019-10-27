package com.mageddo.zipkin.chairfactory.locksmith.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LockSmithChairMountRequestMDB {

	@KafkaListener
	public void consume(String msg){

	}
}
