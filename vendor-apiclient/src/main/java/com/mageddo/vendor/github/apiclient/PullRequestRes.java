package com.mageddo.vendor.github.apiclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record PullRequestRes(

  Integer number,

  String title,

  String state,

  String body,

  @JsonProperty("html_url")
  String htmlUrl,

  Long commits,

  @JsonProperty("changed_files")
  Long changedFiles,

  Long additions,

  Long deletions,

  @JsonProperty("created_at")
  Instant createdAt,

  @JsonProperty("updated_at")
  Instant updatedAt,

  @JsonProperty("closed_at")
  Instant closedAt,

  @JsonProperty("merged_at")
  Instant mergedAt,

  Branch head,

  Branch base

) {

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Branch(

    String ref

  ) {}
}
