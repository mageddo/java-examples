package com.mageddo.httpclientlimiting.brand.resource.reqres;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentAuthorizationReq {
  private UUID id;
  private Long cardNumber;
  private BigDecimal amount;
}
