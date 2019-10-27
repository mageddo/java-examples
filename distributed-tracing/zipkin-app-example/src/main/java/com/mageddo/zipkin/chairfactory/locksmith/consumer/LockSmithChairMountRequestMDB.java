package com.mageddo.zipkin.chairfactory.locksmith.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.locksmith.service.LockSmithService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockSmithChairMountRequestMDB {

	private final LockSmithService lockSmithService;

	@KafkaListener(topics = Topics.FACTORY_LOCKSMITH_CHAIR_MOUNT_REQUEST)
	public void consume(String msg){
		lockSmithService.mountChair(msg);
	}
}
