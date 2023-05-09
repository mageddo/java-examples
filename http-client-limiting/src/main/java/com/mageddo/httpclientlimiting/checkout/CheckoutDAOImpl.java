package com.mageddo.httpclientlimiting.checkout;

import java.util.Random;

import com.mageddo.common.concurrent.concurrent.Threads;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CheckoutDAOImpl implements CheckoutDAO {

  private final Random r = new Random();

  @Override
  public void save(Payment payment) {
    final var stopWatch = StopWatch.createStarted();
    Threads.sleep(30 + r.nextInt(20));
    log.debug("paymentId={}, time={}", payment.getId(), stopWatch.getTime());
  }
}
