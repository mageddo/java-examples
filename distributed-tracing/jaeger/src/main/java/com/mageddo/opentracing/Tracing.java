package com.mageddo.opentracing;

import io.jaegertracing.internal.JaegerObjectFactory;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.thrift.internal.senders.HttpSender;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Tracing {

	private static ThreadLocal<TransactionTracing> transactionTracing = ThreadLocal.withInitial(Tracing::createTracer);

	public static Tracer tracer(){
		return transactionTracing().getTracer();
	}

	public static Span buildSpan(String transactionId, String name){
		tracer(transactionId);
		return buildSpan(name);
	}

	public static Span buildSpan(String name){
		final Span span = tracer().buildSpan(name).start();
		tracer().activateSpan(span);
		return span;
	}

	public static Tracer tracer(String uuid){
		transactionTracing().setUuid(uuid);
		return transactionTracing().getTracer();
	}

	private static TransactionTracing transactionTracing() {
		return transactionTracing.get();
	}

	private static TransactionTracing createTracer() {
		final TextMapCodec codec = TextMapCodec.builder()
			.withUrlEncoding(false)
			.withSpanContextKey("x-trace-id")
			.withObjectFactory(new JaegerObjectFactory())
			.build();

		final UUIDReporter reporter = UUIDReporter.builder()
		.delegate(
			new RemoteReporter.Builder()
				.withFlushInterval(1000)
				.withMaxQueueSize(10)
				.withSender(
					new HttpSender.Builder("http://localhost:14268/api/traces") // http://jaeger-collector.docker
						.build()
				)
				.build()
		)
		.build()
		;
		final JaegerTracer tracer = new JaegerTracer.Builder("CIP")
			.registerExtractor(Format.Builtin.HTTP_HEADERS, codec)
			.registerInjector(Format.Builtin.HTTP_HEADERS, codec)
			.withSampler(new ConstSampler(true))
			.withReporter(reporter)
			.build();
		final TransactionTracing transactionTracing = new TransactionTracing(tracer);
		reporter.setTransactionTracing(transactionTracing);
		return transactionTracing;
	}
}
