package com.mageddo.referrer.tempaltes;

import com.mageddo.referrer.Referrer;

public final class ReferrerTemplates {

  public static final String BANANA_REFERRER_ID = "8f1e6a9a-79a7-4d6a-8e56-e8f2a9d6ce0f";
  public static final String GREEN_BANANA_REFERRER_ID = "2f7a0f3e-4bb4-4ea8-9d3d-e6e9f3f4dfb0";
  public static final String REFERRER_ID = "USER";
  public static final String UPDATED_REFERRER_ID = "SOCIAL";

  private ReferrerTemplates() {
  }

  public static Referrer bananaReferrer() {
    return Referrer.builder()
        .id(BANANA_REFERRER_ID)
        .type(REFERRER_ID)
        .build();
  }

  public static Referrer greenBananaReferrer() {
    return Referrer.builder()
        .id(GREEN_BANANA_REFERRER_ID)
        .type(REFERRER_ID)
        .build();
  }

  public static Referrer greenBananaReferrerUpdated() {
    return Referrer.builder()
        .id(GREEN_BANANA_REFERRER_ID)
        .type(UPDATED_REFERRER_ID)
        .build();
  }
}
