package com.mageddo.opentracing;

import io.opentracing.Tracer;

public class UUIDTracer {

	private Tracer tracer;
	private String uuid;

	public UUIDTracer() {}

	public UUIDTracer setTracer(Tracer tracer) {
		this.tracer = tracer;
		return this;
	}

	public UUIDTracer setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Tracer getTracer() {
		return tracer;
	}

	public String getUuid() {
		return uuid;
	}
}
