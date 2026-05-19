package com.mageddo.vendor.jira.apiclient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
public record IssueChangelogRes(

  Boolean isLast,

  Integer maxResults,

  String nextPage,

  String self,

  Integer startAt,

  Integer total,

  List<Changelog> values

) {

  @Builder
  public record Changelog(

    String id,

    Author author,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    LocalDateTime created,

    List<ChangeItem> items

  ) {

    @Builder
    public record Author(

      String accountId,

      String accountType,

      Boolean active,

      AvatarUrls avatarUrls,

      String displayName,

      String emailAddress,

      String self,

      String timeZone

    ) {}

    @Builder
    public record AvatarUrls(

      @JsonProperty("16x16")
      String size16x16,

      @JsonProperty("24x24")
      String size24x24,

      @JsonProperty("32x32")
      String size32x32,

      @JsonProperty("48x48")
      String size48x48

    ) {}

    @Builder
    public record ChangeItem(

      String field,

      String fieldId,

      String fieldtype,

      String from,

      String fromString,

      String to,

      @JsonProperty("toString")
      String toStr

    ) {}
  }
}
