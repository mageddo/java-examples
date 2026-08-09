package com.mageddo.referrer.dataprovider.doma;

import com.mageddo.referrer.Referrer;

import org.apache.commons.lang3.StringUtils;

public class ReferrerRowMapper {
  public static Referrer toDomain(ReferrerRow row) {
    if (row == null || StringUtils.isAllBlank(row.id(), row.type())) {
      return null;
    }
    return Referrer
        .builder()
        .id(row.id())
        .type(row.type())
        .build();
  }

  public static ReferrerRow of(Referrer referrer) {
    if (referrer == null) {
      return null;
    }
    return ReferrerRow
        .builder()
        .id(referrer.getId())
        .type(referrer.getType())
        .build();
  }
}
