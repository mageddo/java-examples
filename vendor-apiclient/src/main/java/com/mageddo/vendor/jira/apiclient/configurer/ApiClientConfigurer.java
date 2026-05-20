package com.mageddo.vendor.jira.apiclient.configurer;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.WebTarget;
import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Base64;

@RequiredArgsConstructor
public class ApiClientConfigurer {

  public static final String WEB_TARGET = "JiraApiWebTarget";

  @ConfigProperty(name = "vendor.jira.apiclient.base-url")
  String baseUrl;

  @ConfigProperty(name = "vendor.jira.apiclient.email")
  String email;

  @ConfigProperty(name = "vendor.jira.apiclient.api-token")
  String apiToken;

  @Produces
  @Singleton
  @Named(WEB_TARGET)
  public WebTarget webTarget() {
    final var credentials = Base64.getEncoder()
        .encodeToString((this.email + ":" + this.apiToken).getBytes());
    return ClientBuilder
        .newClient()
        .register((ClientRequestFilter) ctx ->
            ctx.getHeaders()
                .add("Authorization", "Basic " + credentials)
        )
        .target(this.baseUrl);
  }
}
