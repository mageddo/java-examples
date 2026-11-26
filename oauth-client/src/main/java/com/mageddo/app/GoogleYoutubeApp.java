package com.mageddo.app;

import com.github.scribejava.core.builder.ScopeBuilder;
import com.mageddo.OAuthAuthenticator;
import com.mageddo.ServiceBuilder;

/**
 * Generates the refresh token used by the cp-youtube-video skill to upload and manage videos.
 */
public class GoogleYoutubeApp {

  public static final String UPLOAD_VIDEOS = "https://www.googleapis.com/auth/youtube.upload";
  public static final String MANAGE_ACCOUNT = "https://www.googleapis.com/auth/youtube";

  public static void main(String[] args) {
    final var scope = new ScopeBuilder()
        .withScopes(UPLOAD_VIDEOS, MANAGE_ACCOUNT)
        .build();

    final var service = ServiceBuilder.build(scope);
    final var authenticator = new OAuthAuthenticator(service);
    try (authenticator) {
      authenticator.auth();
    }
  }

}
