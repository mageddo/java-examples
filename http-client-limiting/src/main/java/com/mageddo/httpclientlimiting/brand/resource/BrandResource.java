package com.mageddo.httpclientlimiting.brand.resource;

import com.mageddo.httpclientlimiting.brand.PaymentAuthorizationService;
import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationReq;

import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationRes;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BrandResource {

  private final PaymentAuthorizationService paymentAuthorizationService;

  @PostMapping(
      path = "/api/v1/brand-payment-authorizations",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public @ResponseBody PaymentAuthorizationRes authorize(@RequestBody PaymentAuthorizationReq paymentReq){
    final var authorized = this.paymentAuthorizationService.authorize(paymentReq);
    return PaymentAuthorizationRes
        .builder()
        .authorized(authorized)
        .build();
  }
}
