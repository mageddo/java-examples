package com.mageddo.lombok.vo.skip_non_null_validation;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class AssetTest {

  @Test(expected = NullPointerException.class)
  public void mustMustValidateNonNull() {

    final var vo = Asset.builder()
        .build();

  }

  @Test
  public void mustMustNotValidateNonNull() {

    final var vo = Asset.builder()
        .unsafeBuild();

    assertNotNull(vo);
  }
}
