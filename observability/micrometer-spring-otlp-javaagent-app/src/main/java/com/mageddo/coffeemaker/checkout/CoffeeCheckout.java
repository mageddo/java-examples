package com.mageddo.coffeemaker.checkout;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "COFFEE_CHECKOUT")
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeCheckout {

  @Id
  @Column(name = "IDT_COFFEE_CHECKOUT")
  private UUID id;

  @Column(name = "NAM_COFFEE")
  private String name;

  @Column(name = "NUM_AMOUNT")
  private BigDecimal amount;

}
