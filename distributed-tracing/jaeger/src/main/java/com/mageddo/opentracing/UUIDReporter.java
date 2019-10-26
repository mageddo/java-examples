package com.mageddo.opentracing;

import io.jaegertracing.internal.JaegerSpan;
import io.jaegertracing.spi.Reporter;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UUIDReporter implements Reporter {

	private final Reporter delegate;

	@Builder.Default
	private final String transactionName = "uuid";

	private TransactionTracing transactionTracing;

	@Override
	public void report(JaegerSpan span) {
		if(this.transactionTracing.getUuid() != null){
			span.setTag(this.transactionName, this.transactionTracing.getUuid());
		}
		this.delegate.report(span);
	}

	@Override
	public void close() {
		this.delegate.close();
	}
}
