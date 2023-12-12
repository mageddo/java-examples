package com.mageddo.shardingsphere.region.presentation;

import java.util.UUID;

import com.mageddo.shardingsphere.region.CustomerRegionService;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ManagedResource
@RequiredArgsConstructor
public class CustomerRegionJMX {

  private final CustomerRegionService customerRegionService;

  @ManagedOperation
  public String create(String customerId, String region) {
    try {
      this.customerRegionService.create(UUID.fromString(customerId), region);
      return "success for customer: " + customerId;
    } catch (Exception e) {
      log.error(
          "status=failed, customerId={}, region={}, msg={}",
          customerId, region, e.getMessage(), e
      );
      return ExceptionUtils.getStackTrace(e);
    }
  }
}
