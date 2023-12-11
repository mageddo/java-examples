package com.mageddo.customer.balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "CUSTOMER_BALANCE")
public class CustomerBalance {

  @Id
  @Column(name = "IDT_CUSTOMER_BALANCE")
  private UUID id;

  @Column(name = "COD_CUSTOMER", unique = true, nullable = false)
  private UUID customerId;

  @Column(name = "NUM_BALANCE", nullable = false)
  private BigDecimal balance;

  @Column(name = "DAT_CREATED", nullable = false)
  private LocalDateTime createdAt;
}
