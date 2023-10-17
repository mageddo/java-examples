package com.mageddo.coffeemaker.checkout;

import java.util.Random;

import com.mageddo.commons.Threads;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoffeeCheckoutService {

  private final Random r = new Random();
  private final CoffeeCheckoutMetrics metrics;
  private final CoffeeCheckoutDomainEventSender domainEventSender;
  private final AcquirerRepository acquirerRepository;
  private final CoffeeCheckoutDAO coffeeCheckoutDAO;

  @WithSpan
  @Transactional
  public void checkout(CoffeeCheckoutReq req) {

    final var stopWatch = StopWatch.createStarted();

//    Threads.sleep(this.r.nextInt(10, 105));
    if(this.r.nextBoolean()){
      Threads.sleep(10);
    } else {
      Threads.sleep(105);
    }
    final var time = stopWatch.getTime();

    this.metrics.getTimesRan().increment(1);
    this.metrics.getTimeToPrepare().record(time);
    this.acquirerRepository.processPayment(req);
    this.coffeeCheckoutDAO.save(req.toCheckout());
    this.domainEventSender.send(req);

    log.info(
        "status=done, time={}, req={}",
        time, req
    );
  }
}
