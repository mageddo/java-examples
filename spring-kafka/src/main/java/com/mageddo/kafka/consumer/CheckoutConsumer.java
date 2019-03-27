package com.mageddo.kafka.consumer;

import com.mageddo.kafka.PlaceboService;
import com.mageddo.kafka.TopicConsumer;
import com.mageddo.kafka.TopicDefinition;
import com.mageddo.kafka.TopicEnum;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.mageddo.kafka.TopicEnum.Constants.TRANSACTION_CHECKOUT_FACTORY;

@Component
public class CheckoutConsumer implements TopicConsumer {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static long lastConsume;
	private static long consumeAvg;
	private long lastRecord;
	private final PlaceboService placeboService;

	public CheckoutConsumer(PlaceboService placeboService) {
		this.placeboService = placeboService;
	}

	@KafkaListener(topics = "#{__listener.topic().getName()}", containerFactory = TRANSACTION_CHECKOUT_FACTORY)
	public void consume(ConsumerRecord<?, byte[]> record) throws Exception {
		final long currentTimeMillis = System.currentTimeMillis();

		placeboService.doSomethingImportant();

		consumeAvg = currentTimeMillis - lastConsume;
		lastConsume = currentTimeMillis;
		this.lastRecord = record.offset();
	}

	@Override
	public TopicDefinition topic() {
		return TopicEnum.TRANSACTION_CHECKOUT.getTopic();
	}

	@Scheduled(fixedDelay = 2000)
	public void checkConsumeAvg(){
		logger.info("consume-avg={}, last-record={}", consumeAvg, lastRecord);
	}

}
