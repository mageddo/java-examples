package com.mageddo.vendor.github.apiclient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

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

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  @JsonProperty("created_at")
  LocalDateTime createdAt,

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  @JsonProperty("updated_at")
  LocalDateTime updatedAt,

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  @JsonProperty("closed_at")
  LocalDateTime closedAt,

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  @JsonProperty("merged_at")
  LocalDateTime mergedAt,

  Branch head,

  Branch base

) {

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Branch(

    String ref

  ) {}
}
