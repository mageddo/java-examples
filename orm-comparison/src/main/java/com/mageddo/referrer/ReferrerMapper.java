package com.mageddo.referrer;

import com.mageddo.referrer.dataprovider.jpa.ReferrerRow;
import org.apache.commons.lang3.StringUtils;

public class ReferrerMapper {

  private ReferrerMapper() {
  }

  public static Referrer toDomain(ReferrerReqV1 req) {
    if (req == null) {
      return null;
    }
    return Referrer.builder()
        .id(req.id())
        .type(req.type())
        .build();
  }

  public static ReferrerReqV1 toReq(Referrer referrer) {
    if (referrer == null) {
      return null;
    }
    return new ReferrerReqV1(referrer.getId(), referrer.getType());
  }

  public static Referrer toDomain(ReferrerRow row) {
    if (row == null || StringUtils.isAllBlank(row.getId(), row.getType())) {
      return null;
    }
    return Referrer.builder()
        .id(row.getId())
        .type(row.getType())
        .build();
  }

  public static ReferrerRow toRow(Referrer referrer) {
    if (referrer == null || StringUtils.isAllBlank(referrer.getId(), referrer.getType())) {
      return null;
    }
    final var row = new ReferrerRow();
    row.setId(referrer.getId());
    row.setType(referrer.getType());
    return row;
  }
}

