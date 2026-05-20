package com.mageddo.vendor.github.apiclient;

import com.mageddo.core.exception.TooManyRequestsException;
import com.mageddo.vendor.github.apiclient.configurer.GithubApiClientConfigurer;
import com.mageddo.ws.rs.ResponseValidator;

import io.github.resilience4j.retry.Retry;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

@Singleton
public class RepoApiClient {

  private final WebTarget webTarget;
  private final Retry retry;

  @Inject
  public RepoApiClient(
      @Named(GithubApiClientConfigurer.WEB_TARGET) WebTarget webTarget,
      @Named(GithubApiClientConfigurer.RETRY) Retry retry
  ) {
    this.webTarget = webTarget;
    this.retry = retry;
  }

  public List<CommitRes> findCommitsSince(String owner, String repo, String branch, Instant since) {
    return Retry.decorateSupplier(this.retry, () -> doFindCommitsSince(owner, repo, branch, since))
        .get();
  }

  private List<CommitRes> doFindCommitsSince(String owner, String repo, String branch, Instant since) {
    final var res = this.webTarget
        .path("/repos/{owner}/{repo}/commits")
        .resolveTemplate("owner", owner)
        .resolveTemplate("repo", repo)
        .queryParam("sha", branch)
        .queryParam("since", since.toString())
        .queryParam("per_page", 100)
        .request()
        .get();
    try (res) {
      checkRateLimit(res);
      ResponseValidator.success(res);
      return res.readEntity(new GenericType<>() {});
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
