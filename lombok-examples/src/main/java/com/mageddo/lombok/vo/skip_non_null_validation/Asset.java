package com.mageddo.lombok.vo.skip_non_null_validation;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "AssetBuilder", buildMethodName = "unsafeBuild")
public class Asset {

  String code;
  BigDecimal grossAmount;
  BigDecimal netAmount;

  public static class AssetBuilder {
    public Asset build(){
      Objects.requireNonNull(this.code, "code must not be null");
      Objects.requireNonNull(this.grossAmount, "code must not be null");
      Objects.requireNonNull(this.netAmount, "code must not be null");
      return this.unsafeBuild();
    }
  }
}
