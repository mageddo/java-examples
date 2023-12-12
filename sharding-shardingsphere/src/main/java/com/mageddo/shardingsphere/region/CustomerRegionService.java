package com.mageddo.shardingsphere.region;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerRegionService {

  private final CustomerRegionRepository customerRegionRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public String findRegionNameByCustomerId(UUID customerId) {
    final var region = this.customerRegionRepository.findByCustomerId(customerId);
    if (region == null) {
      return null;
    }
    return region.getName();
  }
}
