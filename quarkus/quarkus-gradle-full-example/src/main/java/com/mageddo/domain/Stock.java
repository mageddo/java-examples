package com.mageddo.domain;

import java.math.BigDecimal;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Value
@Data
@Builder
//@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class Stock {
  private String symbol;
  private BigDecimal price;
}
