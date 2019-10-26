import brave.Tracing;
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
		var tracing = Tracing.newBuilder()
			.localServiceName("bitskins")
			.spanReporter(spanReporter)
			.build();

		var tracer = tracing.tracer();
		tracer.withSpanInScope(tracer.nextSpan().name("bot-skin-buyer").start());
		tracer.currentSpan().finish();

		tracer.withSpanInScope(tracer.nextSpan().name("find-skins-to-buy"));
		tracer.currentSpan().tag("x", "y");
		Thread.sleep(300);
		tracer.currentSpan().finish();

		tracer.withSpanInScope(tracer.nextSpan().name("calculate-profit"));
		tracer.currentSpan().tag("x", "y");
		Thread.sleep(50);
		tracer.currentSpan().finish();

		tracer.withSpanInScope(tracer.nextSpan().name("check-balance-to-buy"));
		tracer.currentSpan().tag("x", "y");

		Thread.sleep(250);
		tracer.currentSpan().finish();

		tracer.withSpanInScope(tracer.nextSpan().name("send-buy-order"));
		tracer.currentSpan().tag("x", "y");
		Thread.sleep(30);
		tracer.currentSpan().finish();


		tracing.close();
		spanReporter.close();
		sender.close();
	}
}
