package com.mageddo.tracing;

import io.jaegertracing.internal.JaegerObjectFactory;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.thrift.internal.senders.HttpSender;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Tracing {

	private static final ThreadLocal<SpanContext> SPAN_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

	public static Tracer createTracer(final String serviceName){
		final TextMapCodec codec = TextMapCodec.builder()
			.withUrlEncoding(false)
			.withSpanContextKey("x-trace-id")
			.withObjectFactory(new JaegerObjectFactory())
			.build();

		final Reporter reporter = new RemoteReporter.Builder()
			.withFlushInterval(1000)
			.withMaxQueueSize(10)
			.withSender(
				new HttpSender.Builder("http://localhost:14268/api/traces") // http://jaeger-collector.docker
					.build()
			)
			.build();
		return new JaegerTracer.Builder(serviceName)
			.registerExtractor(Format.Builtin.HTTP_HEADERS, codec)
			.registerInjector(Format.Builtin.HTTP_HEADERS, codec)
			.withSampler(new ConstSampler(true))
			.withReporter(reporter)
			.build();
	}

	public static void context(SpanContext context) {
		SPAN_CONTEXT_THREAD_LOCAL.set(context);
	}

	public static SpanContext context(){
		return SPAN_CONTEXT_THREAD_LOCAL.get();
	}
}
