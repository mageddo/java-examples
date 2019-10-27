import brave.Tracing;
import brave.propagation.TraceContext;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		var sender = OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans")
			.toBuilder()
			.connectTimeout(10000)
			.readTimeout(10000)
			.writeTimeout(10000)
			.build()
			;
		var spanReporter = AsyncReporter
			.builder(sender)
			.closeTimeout(5, TimeUnit.SECONDS)
			.build();

		// Create a tracing component with the service name you want to see in Zipkin.
		Tracing.newBuilder()
			.localServiceName("bitskins")
			.spanReporter(spanReporter)
			.build();

		var tracer = Tracing.currentTracer();
		tracer.startScopedSpan("bot-skin-buyer");
		tracer
		.currentSpan()
		.tag("skinId", "14895514112BSL543100268")
		.finish();

		tracer.withSpanInScope(tracer.nextSpan().name("find-skins-to-buy").start());
		tracer
		.currentSpan()
		.tag("x", "y")
		.tag("skinId", "14895514112BSL543100268")
		;
		Thread.sleep(300);
		tracer.currentSpan().finish();

		tracer.startScopedSpan("calculate-profit");
		tracer
		.currentSpan()
		.tag("x", "y")
		.tag("skinId", "14895514112BSL543100268")
		;
		Thread.sleep(50);
		tracer.currentSpan().finish();

		tracer.startScopedSpan("check-balance-to-buy");
		tracer
		.currentSpan()
		.tag("x", "y")
		.tag("skinId", "14895514112BSL543100268")
		;

		Thread.sleep(250);
		tracer.currentSpan().finish();

		tracer.startScopedSpan("send-buy-order");
		tracer
		.currentSpan()
		.tag("x", "y")
		.tag("skinId", "14895514112BSL543100268")
		;
		Thread.sleep(30);
		tracer.currentSpan().finish();
		final var traceContext = Tracing.current().currentTraceContext().get();
		tracer.withSpanInScope(tracer.newChild(
			TraceContext
				.newBuilder()
				.traceId(traceContext.traceId())
				.spanId(traceContext.spanId())
				.build()
		).name("buy-order-confirmation").start());
		sleep(321);
		tracer.currentSpan().finish();

		Tracing.current().close();
		spanReporter.close();
		sender.close();
	}

	public static void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
