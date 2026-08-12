package com.mageddo.referrer;

import io.ebean.bean.ToStringBuilder;
import lombok.Builder;
import lombok.Value;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Value
@Builder
public class Referrer {

  String id;
  String type;

  public static Referrer of(UUID id, String type) {
    final var textId = Objects.toString(id, null);
    if (StringUtils.isAllBlank(textId, type)) {
      return null;
    }
    return new Referrer(textId, type);
  }

  public UUID mustGetUUId() {
    return UUID.fromString(Objects.requireNonNull(this.id, "id must be not null"));
  }

}
