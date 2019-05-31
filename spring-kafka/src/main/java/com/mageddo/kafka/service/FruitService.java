package com.mageddo.kafka.service;

import com.mageddo.kafka.CommitPhase;
import com.mageddo.kafka.producer.MessageSender;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus;

@Service
public class FruitService {

	private final MessageSender messageSender;

	public FruitService(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@Transactional
	public void postOnKafkaPreCommitAndRollback(){
		messageSender.send(new ProducerRecord("fruit", LocalDateTime.now().toString()));
		currentTransactionStatus().setRollbackOnly();
	}

	@Transactional
	public void postOnKafkaAfterCommitAndRollback(){
		messageSender.send(new ProducerRecord("fruit", LocalDateTime.now().toString()), CommitPhase.AFTER_COMMIT);
		currentTransactionStatus().setRollbackOnly();
	}
}
