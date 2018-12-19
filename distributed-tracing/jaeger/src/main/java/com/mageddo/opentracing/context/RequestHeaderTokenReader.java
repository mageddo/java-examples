package com.mageddo.opentracing.context;

import io.opentracing.propagation.TextMap;
import okhttp3.Headers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestHeaderTokenReader implements TextMap {

	private Headers requestHeaders;

	public RequestHeaderTokenReader(Headers requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	@Override
	public Iterator<Map.Entry<String, String>> iterator() {
		final HashMap<String, String> headers = new HashMap<>();
		final Map<String, List<String>> rawHeadersMap = requestHeaders.toMultimap();
		for (Map.Entry<String, List<String>> key : rawHeadersMap.entrySet()) {
			headers.put(key.getKey(), key.getValue().get(0));
		}
		return headers.entrySet().iterator();
	}

	@Override
	public void put(String key, String value) {
		throw new UnsupportedOperationException();
	}
}
