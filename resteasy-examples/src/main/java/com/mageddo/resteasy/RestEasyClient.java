package com.mageddo.resteasy;

import java.io.Closeable;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import jakarta.ws.rs.client.Client;

public class RestEasyClient implements Closeable {

	private final Client client;
	private final PoolingHttpClientConnectionManager pool;
	private final CloseableHttpClient httpClient;

	public RestEasyClient(Client client, PoolingHttpClientConnectionManager pool, CloseableHttpClient httpClient) {
		this.client = client;
		this.pool = pool;
		this.httpClient = httpClient;
	}

	public Client getClient() {
		return client;
	}

	public PoolingHttpClientConnectionManager getPool() {
		return pool;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	@Override
	public void close() {
		client.close();
	}
}
