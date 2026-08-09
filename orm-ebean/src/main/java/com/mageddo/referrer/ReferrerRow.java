package com.mageddo.referrer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReferrerRow {

  @Column(name = "IDT_REFERRER", length = 36)
  String id;

  @Column(name = "IND_REFERRER", length = 36, nullable = false)
  String type;
}
