package com.mageddo.shardingsphere.sharding.infrastructure;

import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TableDiscoverAlgorithm implements StandardShardingAlgorithm<UUID> {

  @Override
  public String getType() {
    return "UUID_HASH";
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<UUID> shardingValue) {
    try {
      log.debug(
          "availableTargetNames={}, shardingValue={}",
          availableTargetNames, shardingValue
      );
      Validate.isTrue(availableTargetNames.size() == 1, "Must have one exact table option");
      return availableTargetNames
          .stream()
          .findFirst()
          .get();
    } catch (Exception e) {
      throw new IllegalStateException(
          "Não foi possível determinar o shard para o UUID: " + shardingValue.getValue(), e
      );
    }
  }

  @Override
  public Collection<String> doSharding(
      Collection<String> availableTargetNames,
      RangeShardingValue<UUID> shardingValue
  ) {
    return null;
  }
}
