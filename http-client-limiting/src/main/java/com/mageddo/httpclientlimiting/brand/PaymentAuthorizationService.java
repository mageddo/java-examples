package com.mageddo.httpclientlimiting.brand;

import java.util.Random;

import com.mageddo.common.concurrent.concurrent.Threads;
import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationReq;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentAuthorizationService {

  private final Random r = new Random();

  public boolean authorize(PaymentAuthorizationReq paymentReq) {

    final var stopWatch = StopWatch.createStarted();
    if (r.nextInt(10) == 1) {
      Threads.sleep(5_000);
    } else {
      Threads.sleep(40);
    }
    final var authorized = r.nextBoolean();
    log.debug("paymentId={}, authorized={}, time={}", paymentReq.getId(), authorized, stopWatch.getTime());
    return authorized;

  }
}
