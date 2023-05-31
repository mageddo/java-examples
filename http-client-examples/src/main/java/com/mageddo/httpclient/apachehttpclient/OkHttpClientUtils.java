package com.mageddo.httpclient.apachehttpclient;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

public class OkHttpClientUtils {
  public static HttpClient build() {
    final var connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(1);
    connectionManager.setDefaultMaxPerRoute(1);
    connectionManager.setDefaultConnectionConfig(ConnectionConfig
        .custom()
        .setConnectTimeout(Timeout.ofMilliseconds(50))
        .setSocketTimeout(Timeout.ofMilliseconds(2000))
        .build()
    );

    final RequestConfig requestConfig = RequestConfig.custom()

        // tempo para esperar por uma conexao do pool
        .setConnectionRequestTimeout(Timeout.ofMilliseconds(100))

        // tempo que irá esperar pela resposta da chamada,
        // sobrescreve o ConnectionConfig..setSocketTimeout
        .setResponseTimeout(Timeout.ofMilliseconds(1000))
        .build();

    final HttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(connectionManager)
        .setDefaultRequestConfig(requestConfig)
        .build();

    return httpClient;
  }
}
