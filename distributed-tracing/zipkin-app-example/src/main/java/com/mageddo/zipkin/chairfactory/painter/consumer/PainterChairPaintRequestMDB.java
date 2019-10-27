package com.mageddo.zipkin.chairfactory.painter.consumer;

import com.mageddo.zipkin.Topics;
import com.mageddo.zipkin.chairfactory.painter.service.PainterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PainterChairPaintRequestMDB {

	private final PainterService painterService;

	@KafkaListener(topics = Topics.FACTORY_PAINTER_CHAIR_PAINT_REQUEST)
	public void consume(String msg){
		painterService.paintChair(msg);
	}
}
