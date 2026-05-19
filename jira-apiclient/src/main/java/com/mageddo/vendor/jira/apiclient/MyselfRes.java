package com.mageddo.vendor.jira.apiclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MyselfRes(

    String accountId,

    String accountType,

    Boolean active,

    ApplicationRoles applicationRoles,

    AvatarUrls avatarUrls,

    String displayName,

    String emailAddress,

    Groups groups,

    String key,

    String name,

    String self,

    String timeZone

) {

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ApplicationRoles(
      List<Object> items,
      Integer size
  ) {}

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
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
  public record Groups(
      List<Object> items,
      Integer size
  ) {}
}
