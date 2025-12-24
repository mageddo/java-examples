package com.mageddo.app;

import com.github.scribejava.core.builder.ScopeBuilder;
import com.mageddo.OAuthAuthenticator;
import com.mageddo.ServiceBuilder;

public class GoogleSheetsApp {

  public static final String READ_SHEETS = "https://www.googleapis.com/auth/spreadsheets.readonly";

  public static void main(String[] args) {
    final var scope = new ScopeBuilder()
        .withScopes(READ_SHEETS)
        .build();

    final var service = ServiceBuilder.build(scope);
    final var authenticator = new OAuthAuthenticator(service);
    try (authenticator) {
      authenticator.auth();
    }

  }

}
