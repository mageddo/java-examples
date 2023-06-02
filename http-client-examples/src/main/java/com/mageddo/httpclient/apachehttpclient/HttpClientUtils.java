package com.mageddo.httpclient.apachehttpclient;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

public class HttpClientUtils {
  public static HttpClient build(PoolingHttpClientConnectionManager connectionManager) {

    final RequestConfig requestConfig = RequestConfig.custom()

        // tempo para esperar por uma conexao do pool
        .setConnectionRequestTimeout(Timeout.ofMilliseconds(100))

        // tempo que irá esperar pela resposta da chamada,
        // sobrescreve o ConnectionConfig..setSocketTimeout
        .setResponseTimeout(Timeout.ofMilliseconds(10_000))
        .build();

    final HttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(connectionManager)
        .setDefaultRequestConfig(requestConfig)
        .build();

    return httpClient;
  }

  static PoolingHttpClientConnectionManager createPool() {
    final var connectionManager = new PoolingHttpClientConnectionManager();
    int conn = 300;
    connectionManager.setMaxTotal(conn);
    connectionManager.setDefaultMaxPerRoute(conn);
    connectionManager.setDefaultConnectionConfig(ConnectionConfig
        .custom()
        .setConnectTimeout(Timeout.ofMilliseconds(2000))
//        .setSocketTimeout(Timeout.ofMilliseconds(2000))
        .build()
    );
    return connectionManager;
  }
}
