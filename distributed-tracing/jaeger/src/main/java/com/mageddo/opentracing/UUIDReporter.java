package com.mageddo.opentracing;

import io.jaegertracing.internal.JaegerSpan;
import io.jaegertracing.spi.Reporter;

public class UUIDReporter implements Reporter {

	private UUIDTracer tracer;
	private Reporter reporter;

	public UUIDReporter(UUIDTracer tracer, Reporter reporter) {
		this.tracer = tracer;
		this.reporter = reporter;
	}

	@Override
	public void report(JaegerSpan span) {
		if(tracer.getUuid() != null){
			span.setTag("uuid", tracer.getUuid());
		}
		reporter.report(span);
	}

	@Override
	public void close() {
		reporter.close();
	}
}
