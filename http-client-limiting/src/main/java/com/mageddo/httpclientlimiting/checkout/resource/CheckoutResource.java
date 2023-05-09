package com.mageddo.httpclientlimiting.checkout.resource;

import java.math.BigDecimal;
import java.util.UUID;

import com.mageddo.httpclientlimiting.checkout.CheckoutService;
import com.mageddo.httpclientlimiting.checkout.resource.reqres.PaymentReq;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CheckoutResource {

  private final CheckoutService checkoutService;

  @PostMapping(
      path = "/api/v1/checkouts",
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public void checkout(PaymentReq paymentReq) {
    this.checkoutService.checkout(paymentReq.toPayment());
  }

  @GetMapping(
      path = "/api/v1/checkouts-mock"
  )
  public void checkoutMock(){
    final var req = PaymentReq
        .builder()
        .id(UUID.randomUUID())
        .merchantId(UUID.randomUUID())
        .amount(BigDecimal.valueOf(Math.random() * 1_000))
        .cardNumber(System.nanoTime())
        .build();
    this.checkout(req);
  }

}
