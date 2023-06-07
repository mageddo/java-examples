package com.mageddo.resteasy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.client.jaxrs.internal.ClientInvocation;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Configuration;

public class RestEasy {

  /**
   * All request time.
   */
  public static final String SOCKET_TIMEOUT = "SOCKET_TIMEOUT";

  /**
   * Time to connect to the server.
   */
  public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";

  /**
   * Time to request a connection from the thread connection pool.
   */
  public static final String CONNECTION_REQUEST_TIMEOUT = "CONNECTION_REQUEST_TIMEOUT";

  public static final Map<HttpResponse, HttpContext> RESPONSES = new LinkedHashMap<>();

  public static Client newClient() {
    return newClient(10);
  }

  public static RestEasyClient newRestEasyClient(int poolSize) {
    final var pool = new PoolingHttpClientConnectionManager();
    pool.setMaxTotal(poolSize);
    // Senao vai estrangular o pool e deixar apenas duas conexoes serem usadas por host
    pool.setDefaultMaxPerRoute(poolSize);
    return newRestEasyClient(pool);
  }

  public static RestEasyClient newRestEasyClient(PoolingHttpClientConnectionManager pool) {
    final CloseableHttpClient httpClient = HttpClientBuilder
        .create()
        .setDefaultRequestConfig(
            RequestConfig.custom()
                .setConnectionRequestTimeout(3_000)
                .setConnectTimeout(500)
                .setSocketTimeout(30_000)
                .build()
        )
        .setConnectionManager(pool)
        .build();
    return new RestEasyClient(
        new ResteasyClientBuilderImpl()
            .httpEngine(withPerRequestTimeout(httpClient))
            .build(),
        pool,
        httpClient
    );
  }

  public static Client newClient(int poolSize) {
    return newRestEasyClient(poolSize).getClient();
  }

  static Integer parseIntegerOrNull(Configuration conf, String key) {
    final Object property = conf.getProperty(key);
    if (property == null) {
      return null;
    }
    return Integer.parseInt(String.valueOf(property));
  }

  /**
   * Allow to set timeout per request
   *
   * @see #CONNECTION_REQUEST_TIMEOUT
   * @see #CONNECT_TIMEOUT
   * @see #SOCKET_TIMEOUT
   */
  static ClientHttpEngine withPerRequestTimeout(HttpClient httpClient) {
    return new ApacheHttpClient43Engine(httpClient) {
      @Override
      protected void loadHttpMethod(ClientInvocation request, HttpRequestBase httpMethod) throws Exception {
        super.loadHttpMethod(request, httpMethod);
        final RequestConfig.Builder reqConf = httpMethod.getConfig() != null ?
            RequestConfig.copy(httpMethod.getConfig()) : RequestConfig.custom();
        final Configuration conf = request.getConfiguration();

        final Integer connectionRequestTimeout = parseIntegerOrNull(conf,
            RestEasy.CONNECTION_REQUEST_TIMEOUT);
        if (connectionRequestTimeout != null) {
          reqConf.setConnectionRequestTimeout(connectionRequestTimeout);
        }

        final Integer connectTimeout = parseIntegerOrNull(conf, RestEasy.CONNECT_TIMEOUT);
        if (connectTimeout != null) {
          reqConf.setConnectTimeout(connectTimeout);
        }

        final Integer socketTimeout = parseIntegerOrNull(conf, RestEasy.SOCKET_TIMEOUT);
        if (socketTimeout != null) {
          reqConf.setSocketTimeout(socketTimeout);
        }
        httpMethod.setConfig(reqConf.build());
      }
    };
  }
}
