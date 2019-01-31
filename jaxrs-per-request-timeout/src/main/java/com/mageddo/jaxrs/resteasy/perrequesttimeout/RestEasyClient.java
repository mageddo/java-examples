package com.mageddo.jaxrs.resteasy.perrequesttimeout;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.ws.rs.client.Client;
import java.io.Closeable;

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
