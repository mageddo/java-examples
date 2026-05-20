package com.mageddo.vendor.github.apiclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequestSearchRes(

  @JsonProperty("total_count")
  Integer totalCount,

  @JsonProperty("incomplete_results")
  Boolean incompleteResults,

  List<Item> items

) {

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Item(

    Integer number,

    String title,

    String state,

    String body,

    @JsonProperty("html_url")
    String htmlUrl,

    @JsonProperty("created_at")
    Instant createdAt,

    @JsonProperty("updated_at")
    Instant updatedAt,

    @JsonProperty("closed_at")
    Instant closedAt,

    @JsonProperty("pull_request")
    PullRequestRef pullRequest

  ) {

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PullRequestRef(

      @JsonProperty("merged_at")
      Instant mergedAt,

      String url

    ) {}
  }
}
