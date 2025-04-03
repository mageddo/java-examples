package com.mageddo.records_and_lombok;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record Fruit (@NonNull String name, int weight) {

}
