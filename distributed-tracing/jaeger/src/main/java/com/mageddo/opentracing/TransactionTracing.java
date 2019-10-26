package com.mageddo.opentracing;

import io.opentracing.Tracer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TransactionTracing {

	private final Tracer tracer;
	private String uuid;

}
