package com.mageddo.shardingsphere.region;

import java.util.UUID;

public interface CustomerRegionRepository {
  CustomerRegion findByCustomerId(UUID customerId);

  void create(CustomerRegion region);
}
