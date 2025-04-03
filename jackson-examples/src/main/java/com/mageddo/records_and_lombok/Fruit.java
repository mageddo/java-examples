package com.mageddo.records_and_lombok;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record Fruit(
    @NonNull String name,
    int weight,
    @JsonProperty("weight_in_kg") double weightInKg
) {

}
