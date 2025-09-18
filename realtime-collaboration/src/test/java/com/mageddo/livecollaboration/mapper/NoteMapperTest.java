package com.mageddo.livecollaboration.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.mageddo.livecollaboration.templates.NoteTemplates;

class NoteMapperTest {

  private final NoteMapper mapper = Mappers.getMapper(NoteMapper.class);

  @Test
  void shouldMapRequestToDomainUsingPathIdentifier() {
    // Arrange
    final var request = NoteTemplates.collaborativeAlpha();

    // Act
    final var note = this.mapper.toDomain("note-123", request);

    // Assert
    assertEquals("note-123", note.getId());
    assertEquals(request.getTitle(), note.getTitle());
    assertEquals(request.getContent(), note.getContent());
    assertIterableEquals(request.getTags(), note.getTags());
  }
}
