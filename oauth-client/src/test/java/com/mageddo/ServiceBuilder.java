package com.mageddo;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.oauth.OAuth20Service;

public class ServiceBuilder {
  public static OAuth20Service build(String scope) {
    final String clientId = System.getenv("GOOGLE_CLIENT_ID");
    final String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
    return new com.github.scribejava.core.builder.ServiceBuilder(clientId)
        .apiSecret(clientSecret)
        .defaultScope(scope)
        .callback("http://localhost:7071/oauth/callback")
        .build(GoogleApi20.instance());
  }
}
