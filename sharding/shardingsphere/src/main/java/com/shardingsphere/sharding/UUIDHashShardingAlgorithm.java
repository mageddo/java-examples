package com.shardingsphere.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.UUID;


public class UUIDHashShardingAlgorithm implements StandardShardingAlgorithm<String> {

    @Override
    public String getType() {
        return "UUID_HASH";
    }

    // verificar singleton
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        UUID uuid = UUID.fromString(shardingValue.getValue());
        int hash = uuid.hashCode();
        int targetIndex = (hash & Integer.MAX_VALUE) % availableTargetNames.size();

        for (String each : availableTargetNames) {
            if (each.endsWith(String.valueOf(targetIndex))) {
                return each;
            }
        }
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
