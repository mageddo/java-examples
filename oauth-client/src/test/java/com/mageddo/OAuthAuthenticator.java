package com.mageddo;

import java.util.Scanner;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

public class OAuthAuthenticator {

  public static final String SECRET_STATE = OAuthAuthenticator.class.getSimpleName();
  private final OAuth20Service service;

  public OAuthAuthenticator(OAuth20Service service) {
    this.service = service;
  }

  public void auth() {
    try {
      this.checkedAuth();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void checkedAuth() throws Exception {
    final Scanner in = new Scanner(System.in, "UTF-8");
    // Obtain the Authorization URL
    System.out.println("Fetching the Authorization URL...");
    final var authorizationUrl = OauthMapper.toAuthorizationUrl(this.service, SECRET_STATE);
    System.out.println("Got the Authorization URL!");
    System.out.println("Now go and authorize ScribeJava here:");
    System.out.println(authorizationUrl);
    System.out.println("And paste the authorization code here");
    System.out.print(">>");
    final String code = in.nextLine();
    System.out.println();

    System.out.println(
        "And paste the state from server here. We have set 'secretState'='" + SECRET_STATE + "'."
    );
    System.out.print(">>");
    final String value = in.nextLine();
    if (SECRET_STATE.equals(value)) {
      System.out.println("State value does match!");
    } else {
      System.out.println("Ooops, state value does not match!");
      System.out.println("Expected = " + SECRET_STATE);
      System.out.println("Got      = " + value);
      System.out.println();
    }

    System.out.println("Trading the Authorization Code for an Access Token...");
    OAuth2AccessToken accessToken = this.service.getAccessToken(code);
    System.out.println("Got the Access Token!");
    System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");

    System.out.println("Refreshing the Access Token...");
    accessToken = this.service.refreshAccessToken(accessToken.getRefreshToken());
    System.out.println("Refreshed the Access Token!");
    System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
    System.out.println();

  }
}
