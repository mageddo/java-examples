package com.mageddo.httpclientlimiting.checkout.brand;

import java.time.Duration;

import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationReq;
import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationRes;
import com.mageddo.httpclientlimiting.checkout.Payment;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BrandClientRest implements BrandClient {

  private final RestTemplate restTemplate;

  @Override
  public boolean authorize(Payment paymentReq) {
    return Failsafe
        .with(
            RetryPolicy.builder()
                .withMaxAttempts(3)
                .withDelay(Duration.ofMillis(40))
                .handle(ResourceAccessException.class)
                .onRetry(it -> {
                  log.debug(
                      "status=retry, attempt={}, paymentId={}",
                      it.getAttemptCount(), paymentReq.getId()
                  );
                })
                .build()
        )
//        .onComplete(it -> System.out.println("completed :" + it))
        .onFailure(it -> {
          log.debug("status=failed, paymentId={}", paymentReq.getId(), it.getException());
        })
        .onSuccess(it -> {
          log.debug("status=success, paymentId={}", paymentReq.getId());
        })
        .get((ctx) -> this.doAuthorize(paymentReq));
  }

  private Boolean doAuthorize(Payment paymentReq) {
    final var req = PaymentAuthorizationReq
        .builder()
        .id(paymentReq.getId())
        .amount(paymentReq.getAmount())
        .cardNumber(paymentReq.getCardNumber())
        .build();
    final var res = this.restTemplate.exchange(
        "http://localhost:8080/api/v1/brand-payment-authorizations",
        HttpMethod.POST,
        new HttpEntity<>(req),
        PaymentAuthorizationRes.class
    );
    final var isSuccess = res.getStatusCode().is2xxSuccessful();
    Validate.isTrue(isSuccess, "Failed to get authorization: %s", res.getStatusCode());
    return res.getBody().getAuthorized();
  }
}
