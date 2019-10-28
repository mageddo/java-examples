package com.mageddo.zipkin.chairfactory.painter.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.painter.service.PainterService;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PainterChairPaintRequestMDB {

	private final PainterService painterService;

	@KafkaListener(topics = Topics.FACTORY_PAINTER_CHAIR_PAINT_REQUEST)
	public void consume(ConsumerRecord<String, String> record){
		final var span = GlobalTracer.get()
		.buildSpan("painter: painting process")
		.asChildOf(TracingKafkaUtils.extractSpanContext(record.headers(), GlobalTracer.get()))
		.withTag("msg", record.value())
		.start()
		;
		try(var scope = GlobalTracer.get().activateSpan(span)) {
			painterService.paintChair(record.value());
		} finally {
			span.finish();
		}
	}
}
