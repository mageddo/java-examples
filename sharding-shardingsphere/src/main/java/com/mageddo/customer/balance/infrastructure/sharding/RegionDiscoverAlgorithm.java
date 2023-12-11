package com.mageddo.customer.balance.infrastructure.sharding;

import java.util.Collection;
import java.util.UUID;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RegionDiscoverAlgorithm implements StandardShardingAlgorithm<String> {

  @Override
  public String getType() {
    return "UUID_HASH";
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames,
      PreciseShardingValue<String> shardingValue) {
    final var uuid = UUID.fromString(shardingValue.getValue());
    log.debug("availableTargetNames={}, shardingValue={}", availableTargetNames, shardingValue);
//        int hash = uuid.hashCode();
//        int targetIndex = (hash & Integer.MAX_VALUE) % availableTargetNames.size();
//
//        for (String each : availableTargetNames) {
//            if (each.endsWith(String.valueOf(targetIndex))) {
//                return each;
//            }
//        }
    throw new UnsupportedOperationException("Não foi possível determinar o shard para o UUID: "
        .concat(shardingValue.getValue()));
  }

  @Override
  public Collection<String> doSharding(
      Collection<String> availableTargetNames,
      RangeShardingValue<String> shardingValue
  ) {
    return null;
  }
}
