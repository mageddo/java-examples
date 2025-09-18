package com.mageddo.livecollaboration.domain;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Note {

  @NonNull
  String id;

  @NonNull
  String title;

  @NonNull
  String content;

  @NonNull
  List<String> tags;

  Note(
      final String id,
      final String title,
      final String content,
      final List<String> tags
  ) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.tags = List.copyOf(tags);
  }
}
