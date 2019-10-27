package com.mageddo.zipkin.chairfactory.painter.consumer;

import brave.kafka.clients.KafkaTracing;
import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.painter.service.PainterService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PainterChairPaintRequestMDB {

	private final PainterService painterService;
	private final KafkaTracing kafkaTracing;

	@KafkaListener(topics = Topics.FACTORY_PAINTER_CHAIR_PAINT_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = kafkaTracing.nextSpan(record)
		.name("painter: painting process")
		.tag("msg", record.value())
		;
		painterService.paintChair(record.value());
		span.finish();
	}
}
