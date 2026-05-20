package com.mageddo.vendor.github.apiclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommitRes(

  String sha,

  Commit commit

) {

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Commit(

    Author author

  ) {}

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Author(

    String name,

    @JsonProperty("date")
    Instant date

  ) {}
}
