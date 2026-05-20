package com.mageddo.vendor.github.apiclient;

import com.mageddo.vendor.github.apiclient.configurer.GithubApiClientConfigurer;
import com.mageddo.core.exception.TooManyRequestsException;
import com.mageddo.ws.rs.ResponseValidator;

import io.github.resilience4j.retry.Retry;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Singleton
public class PullRequestApiClient {

  private final WebTarget webTarget;
  private final Retry retry;

  @Inject
  public PullRequestApiClient(
      @Named(GithubApiClientConfigurer.WEB_TARGET) WebTarget webTarget,
      @Named(GithubApiClientConfigurer.RETRY) Retry retry
  ) {
    this.webTarget = webTarget;
    this.retry = retry;
  }

  public PullRequestSearchRes findByOrg(String org, List<String> issueCodes, int page) {
    return Retry.decorateSupplier(this.retry, () -> doSearchByOrg(org, issueCodes, page))
        .get();
  }

  public PullRequestRes findByRepo(String owner, String repo, int number) {
    return Retry.decorateSupplier(this.retry, () -> doFindByRepo(owner, repo, number))
        .get();
  }

  private PullRequestSearchRes doSearchByOrg(String org, List<String> issueCodes, int page) {
    final var terms = String.join(" OR ", issueCodes);
    final var query = "org:%s is:pr (%s) in:title".formatted(org, terms);
    final var res = this.webTarget
        .path("/search/issues")
        .queryParam("q", query)
        .queryParam("per_page", 100)
        .queryParam("page", page)
        .request()
        .get();
    try (res) {
      checkRateLimit(res);
      ResponseValidator.success(res);
      return res.readEntity(PullRequestSearchRes.class);
    }
  }

  private PullRequestRes doFindByRepo(String owner, String repo, int number) {
    final var res = this.webTarget
        .path("/repos/{owner}/{repo}/pulls/{number}")
        .resolveTemplate("owner", owner)
        .resolveTemplate("repo", repo)
        .resolveTemplate("number", number)
        .request()
        .get();
    try (res) {
      checkRateLimit(res);
      ResponseValidator.success(res);
      return res.readEntity(PullRequestRes.class);
    }
  }

  private void checkRateLimit(Response res) {
    if (res.getStatus() != 429) {
      return;
    }
    final var body = res.hasEntity() ? res.readEntity(String.class) : null;
    throw new TooManyRequestsException(body);
  }
}
