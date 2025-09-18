package com.mageddo.livecollaboration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mageddo.livecollaboration.domain.Note;
import com.mageddo.livecollaboration.resource.vo.NoteReq;

@Mapper(componentModel = "cdi")
public interface NoteMapper {

  @Mapping(target = "id", source = "noteId")
  Note toDomain(String noteId, NoteReq request);
}
