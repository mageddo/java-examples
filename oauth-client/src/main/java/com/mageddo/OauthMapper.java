package com.mageddo;

import java.util.HashMap;
import java.util.Map;

import com.github.scribejava.core.oauth.OAuth20Service;

public class OauthMapper {
  public static String toAuthorizationUrl(OAuth20Service service, String secretState) {
    final Map<String, String> additionalParams = new HashMap<>();
    additionalParams.put("access_type", "offline");
    //force to reget refresh token (if user are asked not the first time)
    additionalParams.put("prompt", "consent");
    return service
        .createAuthorizationUrlBuilder()
        .state(secretState)
        .additionalParams(additionalParams)
        .build();
  }
}
