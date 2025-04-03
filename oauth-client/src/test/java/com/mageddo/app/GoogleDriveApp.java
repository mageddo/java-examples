package com.mageddo.app;

import com.github.scribejava.core.builder.ScopeBuilder;
import com.mageddo.OAuthAuthenticator;
import com.mageddo.ServiceBuilder;

public class GoogleDriveApp {

  public static void main(String[] args) {
    final var scope = new ScopeBuilder()
        .withScopes("https://www.googleapis.com/auth/drive.file")
        .build();
    final var service = ServiceBuilder.build(scope);

    new OAuthAuthenticator(service).auth();

  }

}
