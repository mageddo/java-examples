package com.mageddo.httpclientlimiting.brand;

import java.util.Random;
import java.util.concurrent.ExecutorService;

import com.mageddo.common.concurrent.concurrent.ThreadPool;
import com.mageddo.common.concurrent.concurrent.Threads;
import com.mageddo.httpclientlimiting.brand.resource.reqres.PaymentAuthorizationReq;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentAuthorizationService {

  private final Random r = new Random();
  private final ExecutorService pool = ThreadPool.newFixed(Integer.MAX_VALUE);

  public boolean authorize(PaymentAuthorizationReq paymentReq) {
    return this.doAuthorize(paymentReq);
//    try {
//      return this.pool.submit(() -> this.doAuthorize(paymentReq)).get(100, TimeUnit.MILLISECONDS);
//    } catch (InterruptedException | ExecutionException | TimeoutException e) {
//      throw new RuntimeException(e);
//    }
  }

  private boolean doAuthorize(PaymentAuthorizationReq paymentReq) {
    final var stopWatch = StopWatch.createStarted();
    if (r.nextInt(10) == 1) {
      Threads.sleep(5_000);
    } else {
      Threads.sleep(40);
    }
    final var authorized = r.nextBoolean();
    log.debug("paymentId={}, authorized={}, time={}", paymentReq.getId(), authorized,
        stopWatch.getTime());
    return authorized;
  }
}
