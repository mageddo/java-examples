package com.mageddo.opentracing.context;

import okhttp3.Headers;

import java.util.Iterator;
import java.util.Map;

public class RequestHeaderTokenWriter implements io.opentracing.propagation.TextMap {

	private final Headers.Builder builder;

	public RequestHeaderTokenWriter(Headers.Builder builder) {
		this.builder = builder;
	}

	@Override
	public Iterator<Map.Entry<String, String>> iterator() {
		throw new UnsupportedOperationException("carrier is write-only");
	}

	@Override
	public void put(String key, String value) {
		builder.add(key, value);
	}
}
