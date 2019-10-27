package com.mageddo.kafka;

import com.mageddo.tracing.HeadersMapInjectAdapter;
import com.mageddo.tracing.Tracing;
import io.opentracing.propagation.Format;
import io.opentracing.util.GlobalTracer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@RequiredArgsConstructor
public class MessageSender {

	private final KafkaTemplate kafkaTemplate;

	public ListenableFuture send(ProducerRecord r){
		if(Tracing.context() != null) {
			GlobalTracer.get().inject(
				Tracing.context(), Format.Builtin.TEXT_MAP,
				new HeadersMapInjectAdapter(r.headers())
			);
		}
		return kafkaTemplate.send(r);
	}
}
