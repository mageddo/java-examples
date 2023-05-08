package com.mageddo.httpclientlimiting.checkout;

import com.mageddo.httpclientlimiting.checkout.Payment.Status;
import com.mageddo.httpclientlimiting.checkout.brand.BrandClient;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {

  private final BrandClient brandClient;
  private final CheckoutDAO checkoutDAO;

  public void checkout(Payment payment){
    final var stopWatch = StopWatch.createStarted();
    this.doAuthorize(payment);
    this.checkoutDAO.save(payment);
    log.debug("paymentId={}, time={}", payment.getId(), stopWatch.getTime());
  }

  void doAuthorize(Payment payment) {
    final var authorized = this.brandClient.authorize(payment);
    if(authorized){
      payment.setStatus(Status.SCHEDULED);
    } else {
      payment.setStatus(Status.CANCELED);
    }
  }
}
