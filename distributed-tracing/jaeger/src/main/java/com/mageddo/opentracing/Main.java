package com.mageddo.opentracing;

import com.mageddo.opentracing.context.RequestHeaderTokenReader;
import com.mageddo.opentracing.context.RequestHeaderTokenWriter;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import lombok.val;
import okhttp3.Headers;

import java.util.UUID;

import static com.mageddo.opentracing.Tracing.buildSpan;
import static com.mageddo.opentracing.Tracing.tracer;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		final String transactionId = UUID.randomUUID().toString();
		blockLiquidationValue(transactionId);
		createTransactionOnMonolith();
		generateLiquidationFile();

//		tracer().inject(tracer().activeSpan().context(), Format.Builtin.TEXT_MAP, new TextMapInjectAdapter());


		final Headers.Builder headers = new Headers.Builder();
		tracer().inject(tracer().activeSpan().context(), Format.Builtin.HTTP_HEADERS, new RequestHeaderTokenWriter(headers));


		Thread t1 = new Thread(() -> {
			tracer(transactionId)
				.buildSpan("final-step")
				.asChildOf(createContextFromHeaders(headers.build()))
				.start()
				.finish();
		});
		t1.start();

		t1.join();

	}

	static SpanContext createContextFromHeaders(Headers requestHeaders) {
		return tracer().extract(Format.Builtin.HTTP_HEADERS, new RequestHeaderTokenReader(requestHeaders));
	}

	private static void blockLiquidationValue(String transactionId) throws InterruptedException {
		buildSpan(transactionId, "block-withdraw-value")
		.setTag("monitor", "atm-withdraw")
		;

//		tracer().scopeManager().active().span().setBaggageItem("general-id", "321");

		Thread.sleep(25L);

		tracer().activeSpan().finish();
	}


	private static void createTransactionOnMonolith() throws InterruptedException {
		buildSpan("monolith-transaction-creation")
		.setTag("monitor", "atm-withdraw")
		;

		tracer().activeSpan().log("locking transaction");
		Thread.sleep(200L);
		tracer().activeSpan().log("creating transaction");

		tracer().activeSpan().finish();
	}


	private static void generateLiquidationFile() throws InterruptedException {
		val span = tracer().buildSpan("liquidation-file-generation").start();
		tracer().activateSpan(span);
		span.setTag("monitor", "atm-withdraw");

		Thread.sleep(50L);
		tracer().activeSpan().finish();
	}

}

