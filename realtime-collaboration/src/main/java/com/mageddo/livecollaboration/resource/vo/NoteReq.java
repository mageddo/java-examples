package com.mageddo.livecollaboration.resource.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteReq {

  @NonNull
  String id;

  @NonNull
  String title;

  @NonNull
  String content;

  @Default
  @NonNull
  List<String> tags = List.of();
}
