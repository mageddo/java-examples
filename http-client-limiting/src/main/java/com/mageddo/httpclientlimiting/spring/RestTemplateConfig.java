package com.mageddo.httpclientlimiting.spring;

import java.time.Duration;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate() {
    final var connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(20);
    connectionManager.setDefaultMaxPerRoute(20);

    final var requestConfig = RequestConfig
        .custom()
        .setConnectionRequestTimeout(Timeout.of(Duration.ofMillis(30)))
        .setResponseTimeout(Timeout.of(Duration.ofMillis(50)))
        .setConnectTimeout(Timeout.of(Duration.ofMillis(50)))
        .build();

    final var httpClient = HttpClientBuilder
        .create()
        .setConnectionManager(connectionManager)
        .setDefaultRequestConfig(requestConfig)
        .build();

    final var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(requestFactory);
  }
}
