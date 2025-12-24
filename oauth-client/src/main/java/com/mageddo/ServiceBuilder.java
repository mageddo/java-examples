package com.mageddo;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.mageddo.server.CallbackServer;

public class ServiceBuilder {

  public static OAuth20Service build(String scope) {
    return build(scope, CallbackServer.PORT);
  }

  public static OAuth20Service build(String scope, int serverPort) {
    final String clientId = System.getenv("GOOGLE_CLIENT_ID");
    final String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
    return new com.github.scribejava.core.builder.ServiceBuilder(clientId)
        .apiSecret(clientSecret)
        .defaultScope(scope)
        .callback(String.format("http://localhost:%d/oauth/callback", serverPort))
        .build(GoogleApi20.instance());
  }
}
