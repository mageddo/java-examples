package com.mageddo.opentracing;

import io.jaegertracing.internal.JaegerObjectFactory;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.propagation.TextMapCodec;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.thrift.internal.senders.HttpSender;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;

public final class Tracing {

	private static ThreadLocal<UUIDTracer> tracers = ThreadLocal.withInitial(Tracing::createTracer);

	private Tracing() {}

	public static Tracer tracer(){
		return getTracer().getTracer();
	}

	public static Tracer tracer(String uuid){
		getTracer().setUuid(uuid);
		return getTracer().getTracer();
	}

	private static UUIDTracer getTracer() {
		return tracers.get();
	}

	public static UUIDTracer createTracer() {

		final TextMapCodec codec = TextMapCodec.builder()
			.withUrlEncoding(false)
			.withSpanContextKey("x-trace-id")
			.withObjectFactory(new JaegerObjectFactory())
			.build();

		final UUIDTracer uuidTracer = new UUIDTracer();
		final Tracer tracer = new JaegerTracer.Builder("CIP")
			.registerExtractor(Format.Builtin.HTTP_HEADERS, codec)
			.registerInjector(Format.Builtin.HTTP_HEADERS, codec)
			.withSampler(new ConstSampler(true))
			.withReporter(
				new UUIDReporter(
					uuidTracer,
					new RemoteReporter.Builder()
						.withFlushInterval(1000)
						.withMaxQueueSize(10)
						.withSender(
							new HttpSender.Builder("http://localhost:14268/api/traces") // http://jaeger-collector.docker
								.build()
						)
						.build()
				)
			)
			.build();
		return uuidTracer.setTracer(tracer);
	}
}
