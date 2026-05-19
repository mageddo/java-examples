package com.mageddo.vendor.jira.apiclient;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

import java.util.Base64;

public class ApiClientConfigurer {

  public static WebTarget webTarget() {
    final var baseUrl = System.getenv("JIRA_BASE_URL");
    final var email = System.getenv("JIRA_EMAIL");
    final var apiToken = System.getenv("JIRA_API_TOKEN");
    final var credentials = Base64.getEncoder().encodeToString((email + ":" + apiToken).getBytes());
    return ClientBuilder
        .newClient()
        .register((jakarta.ws.rs.client.ClientRequestFilter) ctx ->
            ctx.getHeaders().add("Authorization", "Basic " + credentials)
        )
        .target(baseUrl);
  }
}
