package com.mageddo.shardingsphere.region;

import java.util.UUID;

public class CustomerRegionFactory {
  public static CustomerRegion buildForCreation(UUID customerId, String region) {
    return CustomerRegion
        .builder()
        .id(customerId)
        .name(region)
        .customerId(customerId)
        .build()
        ;
  }
}
