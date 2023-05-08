package com.mageddo.httpclientlimiting.checkout.resource.reqres;

import java.math.BigDecimal;
import java.util.UUID;

import com.mageddo.httpclientlimiting.checkout.Payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentReq {

  private UUID id;
  private UUID merchantId;
  private Long cardNumber;
  private BigDecimal amount;

  public Payment toPayment() {
    return Payment
        .builder()
        .id(this.id)
        .amount(this.amount)
        .cardNumber(this.cardNumber)
        .status(Payment.Status.INITIAL)
        .merchantId(this.merchantId)
        .build()
        ;
  }
}

