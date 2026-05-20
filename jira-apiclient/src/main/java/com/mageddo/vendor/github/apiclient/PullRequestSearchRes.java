package com.mageddo.vendor.github.apiclient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("updated_at")
    LocalDateTime updatedAt,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("closed_at")
    LocalDateTime closedAt,

    @JsonProperty("pull_request")
    PullRequestRef pullRequest

  ) {

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PullRequestRef(

      @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
      @JsonProperty("merged_at")
      LocalDateTime mergedAt,

      String url

    ) {}
  }
}
