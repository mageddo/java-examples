package com.mageddo.httpclientlimiting.brand;

import java.util.Random;

import com.mageddo.common.concurrent.concurrent.Threads;

import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationReq;

import org.apache.commons.lang3.time.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentAuthorizationService {

  private final Random r = new Random();

  public void authorize(PaymentAuthorizationReq paymentReq) {

    final var stopWatch = StopWatch.createStarted();
    if (r.nextInt(10) == 1) {
      Threads.sleep(5_000);
    } else {
      Threads.sleep(40);
    }
    log.debug("paymentId={}, time={}", paymentReq.getId(), stopWatch.getTime());

  }
}
