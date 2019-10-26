import brave.Tracing;
import brave.propagation.TraceContext;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.time.LocalDateTime;
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
		var tracing = Tracing.newBuilder()
			.localServiceName("bitskins")
			.spanReporter(spanReporter)
			.build();

// Tracing exposes objects you might need, most importantly the tracer
		var tracer = tracing.tracer();
//		tracer
//			.startScopedSpan("xyz")
//			.tag("size", "9")
//			.tag("name", "Maria")
//			.annotate("some-annotation")
//			.error(new Exception("some unbelievable error"))
//			.finish();
//
//		tracer.
		tracer.withSpanInScope(tracer.nextSpan().name("bot-skin-buyer").start());
		tracer.startScopedSpan("find-skins-to-buy").tag("x", "y").finish();
		Thread.sleep(300);
		tracer.startScopedSpan("calculate-profit").tag("x", "y").finish();
		Thread.sleep(50);
		tracer.startScopedSpan("check-balance-to-buy").tag("x", "y").finish();
		Thread.sleep(250);
		tracer.startScopedSpan("send-buy-order").tag("x", "y").finish();
		Thread.sleep(30);
		tracer.currentSpan().finish();

// example 2

		tracer.withSpanInScope(tracer.nextSpan().name("price-sync").tag("bskItemCode", "14895514112BSL543100268").start());
		tracer.startScopedSpan("skin-buy").tag("bskItemCode", "14895514112BSL543100268").tag("status", "started").finish();
		Thread.sleep(30);
		tracer.startScopedSpan("skin-buy").tag("bskItemCode", "14895514112BSL543100268").tag("status", "success").tag("rule", "min-profit-met").finish();
		Thread.sleep(50);
		tracer.startScopedSpan("iem-negotiator").tag("bskItemCode", "14895514112BSL543100268").tag("status", "negotiated").tag("value", "10.99").finish();
		Thread.sleep(250);
		tracer.startScopedSpan("item-negotiator").tag("bskItemCode", "14895514112BSL543100268").tag("status", "failed").tag("rule", "same-price-rule").finish();
		tracer.currentSpan().finish();

// example 2.1 -- nao precisa passar o id do item toda vez dado que as spans estão amarradas ao pai
		tracer.withSpanInScope(
			tracer
				.nextSpan()
				.name("price-sync")
				.tag("app", "CSGO")
				.tag("bskItemCode", "14895514112BSL543100268")
				.start()
		);
		tracer.startScopedSpan("skin-buy")
			.tag("status", "started")
			.finish();

		Thread.sleep(30);

		tracer.startScopedSpan("skin-buy")
			.tag("status", "success")
			.tag("rule", "min-profit-met")
			.finish();

		Thread.sleep(50);

		tracer.startScopedSpan("iem-negotiator")
			.tag("status", "negotiated")
			.tag("value", "10.99")
			.finish();

		Thread.sleep(250);
		tracer.startScopedSpan("item-negotiator")
			.tag("status", "failed")
			.tag("rule", "same-price-rule")
			.finish();


// example 3 referring an previous parent
		tracer.withSpanInScope(tracer.joinSpan(TraceContext.newBuilder().traceId(-2792739479104120024L).spanId(6636156556104264803L).build()));
		tracer.startScopedSpan("rule1Check").tag("bskItemCode", "14895514112BSL543100268").finish();
		Thread.sleep(30);
		tracer.startScopedSpan("rule2Check").tag("bskItemCode", "14895514112BSL543100268").finish();
		Thread.sleep(50);
		tracer.startScopedSpan("rule3Check").tag("bskItemCode", "14895514112BSL543100268").finish();
		Thread.sleep(250);
		tracer.startScopedSpan("rule4Check").tag("bskItemCode", "14895514112BSL543100268").finish();
		Thread.sleep(10);
		tracer.startScopedSpan("rule5Check").tag("bskItemCode", LocalDateTime.now().toString()).finish();
		Thread.sleep(30);
		tracer.currentSpan().finish();

		tracing.close();
		spanReporter.close();
		sender.close();
	}
}
