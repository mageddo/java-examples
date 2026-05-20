package com.mageddo.vendor.github.apiclient.configurer;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.WebTarget;

import com.mageddo.core.exception.TooManyRequestsException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

@RequiredArgsConstructor
public class GithubApiClientConfigurer {

  public static final String WEB_TARGET = "GithubApiWebTarget";
  public static final String RETRY = "GithubRetry";

  @ConfigProperty(name = "vendor.github.apiclient.base-url")
  String baseUrl;

  @ConfigProperty(name = "vendor.github.apiclient.token")
  String token;

  @Produces
  @Singleton
  @Named(WEB_TARGET)
  public WebTarget webTarget() {
    return ClientBuilder
        .newClient()
        .register((ClientRequestFilter) ctx ->
            ctx.getHeaders()
                .add("Authorization", "Bearer " + this.token)
        )
        .target(this.baseUrl);
  }

  @Produces
  @Singleton
  @Named(RETRY)
  public Retry retry() {
    final var config = RetryConfig.custom()
        .maxAttempts(5)
        .waitDuration(Duration.ofSeconds(61))
        .retryExceptions(TooManyRequestsException.class)
        .build();
    return Retry.of("github-rate-limit", config);
  }
}
