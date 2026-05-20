package com.mageddo.vendor.jira.apiclient;

import com.mageddo.vendor.jira.apiclient.configurer.ApiClientConfigurer;
import com.mageddo.ws.rs.ResponseValidator;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.WebTarget;
import lombok.RequiredArgsConstructor;

@Singleton
public class MyselfApiClient {

  private final WebTarget webTarget;

  @Inject
  public MyselfApiClient(@Named(ApiClientConfigurer.WEB_TARGET) WebTarget webTarget) {
    this.webTarget = webTarget;
  }

  public MyselfRes find() {
    final var res = this.webTarget
        .path("/rest/api/3/myself")
        .request()
        .get();

    try (res) {
      ResponseValidator.success(res);
      return res.readEntity(MyselfRes.class);
    }
  }
}
