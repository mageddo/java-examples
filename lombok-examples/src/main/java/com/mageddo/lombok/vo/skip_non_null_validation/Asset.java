package com.mageddo.lombok.vo.skip_non_null_validation;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Asset {
  @NonNull
  String code;

  @NonNull
  BigDecimal grossAmount;

  @NonNull
  BigDecimal netAmount;
}
