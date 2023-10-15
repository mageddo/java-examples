package com.mageddo.coffeemaker.checkout;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AcquirerRepositoryRest implements AcquirerRepository {

  private final RestTemplate restTemplate;

  @Override
  public void processPayment(CoffeeCheckoutReq req) {
    try {
      this.restTemplate.getForEntity(
          "https://webhook.site/b93ccea6-147d-4e68-b1d0-c36b7a30dd67",
          String.class
      );
    } catch (Exception e) {
      log.error("status=paymentProcessFailed, req={}", req, e);
    }
  }
}
