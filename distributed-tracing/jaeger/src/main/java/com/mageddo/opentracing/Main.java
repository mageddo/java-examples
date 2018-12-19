package com.mageddo.opentracing;

import com.mageddo.opentracing.context.RequestHeaderTokenReader;
import com.mageddo.opentracing.context.RequestHeaderTokenWriter;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import okhttp3.Headers;

import java.util.*;

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
		tracer(transactionId)
			.buildSpan("block-withdraw-value")
			.withTag("monitor", "atm-withdraw")
			.startActive(true).span()
		;

//		tracer().scopeManager().active().span().setBaggageItem("general-id", "321");

		Thread.sleep(25L);

		tracer().activeSpan().finish();
	}


	private static void createTransactionOnMonolith() throws InterruptedException {
		tracer().buildSpan("monolith-transaction-creation")
			.withTag("monitor", "atm-withdraw")
			.startActive(true)
		;

		tracer().activeSpan().log("locking transaction");
		Thread.sleep(200L);
		tracer().activeSpan().log("creating transaction");

		tracer().activeSpan().finish();
	}


	private static void generateLiquidationFile() throws InterruptedException {
		tracer().buildSpan("liquidation-file-generation")
			.withTag("monitor", "atm-withdraw")
			.startActive(true)
		;
		Thread.sleep(50L);
		tracer().activeSpan().finish();
	}

}

