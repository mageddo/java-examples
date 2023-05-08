package com.mageddo.httpclientlimiting.brand.resource.reqres;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuthorizationReq {
  private UUID id;
  private Long cardNumber;
  private BigDecimal amount;
}
