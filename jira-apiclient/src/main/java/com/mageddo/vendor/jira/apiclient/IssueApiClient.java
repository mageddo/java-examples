package com.mageddo.vendor.jira.apiclient;

import com.mageddo.vendor.jira.apiclient.configurer.ApiClientConfigurer;
import com.mageddo.ws.rs.ResponseValidator;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.WebTarget;
import lombok.RequiredArgsConstructor;

@Singleton
public class IssueApiClient {

  private final WebTarget webTarget;

  @Inject
  public IssueApiClient(@Named(ApiClientConfigurer.WEB_TARGET) WebTarget webTarget) {
    this.webTarget = webTarget;
  }

  public IssueChangelogRes findChangelog(String issueKey) {
    final var res = this.webTarget
        .path("/rest/api/3/issue/{issueKey}/changelog")
        .resolveTemplate("issueKey", issueKey)
        .request()
        .get();

    try (res) {
      ResponseValidator.success(res);
      return res.readEntity(IssueChangelogRes.class);
    }
  }
}
