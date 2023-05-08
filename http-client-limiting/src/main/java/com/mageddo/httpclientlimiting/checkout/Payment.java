package com.mageddo.httpclientlimiting.checkout;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Payment {

  @NonNull
  private final UUID id;

  @NonNull
  private final UUID merchantId;

  @NonNull
  private final Long cardNumber;

  @NonNull
  private final BigDecimal amount;

  @NonNull
  private Status status;

  public enum Status {
    INITIAL,
    SCHEDULED,
    CANCELED,
    ;
  }
}
