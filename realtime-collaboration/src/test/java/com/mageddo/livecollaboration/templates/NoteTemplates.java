package com.mageddo.livecollaboration.templates;

import java.util.List;

import com.mageddo.livecollaboration.resource.vo.NoteReq;

public final class NoteTemplates {

  private NoteTemplates() {
  }

  public static NoteReq collaborativeAlpha() {
    return NoteReq.builder()
        .id("note-alpha")
        .title("Título compartilhado")
        .content("Conteúdo inicial da nota colaborativa.")
        .tags(List.of("colab", "yjs"))
        .build();
  }
}
