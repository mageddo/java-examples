package com.mageddo.shardingsphere.sharding.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.mageddo.shardingsphere.infrastructure.AppContext;
import com.mageddo.shardingsphere.region.CustomerRegionService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RegionDiscoverAlgorithm implements StandardShardingAlgorithm<UUID> {

  @Override
  public String getType() {
    return "UUID_HASH";
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<UUID> shardingValue) {
    final var customerId = shardingValue.getValue();
    if (isCustomerRegionTable(shardingValue)) {
      return "r1";
    }
    try {

      final var service = AppContext.context().getBean(CustomerRegionService.class);
      final var regionName = service.findRegionNameByCustomerId(customerId);
      if (StringUtils.isNotBlank(regionName)) {
        log.debug(
            "status=decidedByDatabaseRecord, region={}, availableTargetNames={}, shardingValue={}",
            regionName, availableTargetNames, shardingValue
        );
        return regionName;
      }

      final var index = Math.abs(customerId.hashCode()) % availableTargetNames.size();
      final var region = new ArrayList<>(availableTargetNames).get(index);
      log.debug(
          "status=decidedByHash, region={}, availableTargetNames={}, shardingValue={}, index={}",
          region, availableTargetNames, shardingValue, index
      );
      return region;
    } catch (Exception e) {
      throw new IllegalStateException(
          "Não foi possível determinar o shard para o UUID: " + customerId, e
      );
    }
  }

  private static boolean isCustomerRegionTable(PreciseShardingValue<UUID> shardingValue) {
    return StringUtils.equalsIgnoreCase(shardingValue.getLogicTableName(), "customer_region");
  }

  @Override
  public Collection<String> doSharding(
      Collection<String> availableTargetNames,
      RangeShardingValue<UUID> shardingValue
  ) {
    return null;
  }
}
