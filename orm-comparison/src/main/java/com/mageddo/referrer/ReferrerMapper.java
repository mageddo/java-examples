package com.mageddo.referrer;

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

}

