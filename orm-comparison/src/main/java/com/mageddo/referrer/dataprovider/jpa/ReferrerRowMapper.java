package com.mageddo.referrer.dataprovider.jpa;

import com.mageddo.referrer.Referrer;

import org.apache.commons.lang3.StringUtils;

public class ReferrerRowMapper {
  public static Referrer toDomain(ReferrerRow row) {
    if (row == null || StringUtils.isAllBlank(row.getId(), row.getType())) {
      return null;
    }
    return Referrer
        .builder()
        .id(row.getId())
        .type(row.getType())
        .build();
  }

  public static ReferrerRow of(Referrer referrer) {
    if (referrer == null) {
      return null;
    }
    final var row = new ReferrerRow();
    row.setId(referrer.getId());
    row.setType(referrer.getType());
    return row;
  }
}
