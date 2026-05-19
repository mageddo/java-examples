package com.mageddo.vendor.jira.apiclient;

import com.example.ResponseValidator;
import jakarta.ws.rs.client.WebTarget;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyselfApiClient {

  private final WebTarget webTarget;

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
