package com.mageddo.shardingsphere.customer.balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "CUSTOMER_BALANCE_HISTORY")
public class CustomerBalanceHistory {

  @Id
  @Column(name = "IDT_CUSTOMER_BALANCE_HISTORY")
  private UUID id;

  @Column(name = "IDT_CUSTOMER_BALANCE")
  private UUID customerBalanceId;

  @Column(name = "COD_CUSTOMER", unique = true, nullable = false)
  private UUID customerId;

  @Column(name = "NUM_BALANCE", nullable = false)
  private BigDecimal balance;

  @Column(name = "DAT_CREATED", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "DAT_CUSTOMER_BALANCE_UPDATED", nullable = false)
  private LocalDateTime customerBalanceUpdatedAt;

  public static CustomerBalanceHistory of(CustomerBalance customerBalance) {
    return CustomerBalanceHistory
        .builder()
        .id(UUID.randomUUID())
        .customerBalanceId(customerBalance.getId())
        .balance(customerBalance.getBalance())
        .customerId(customerBalance.getCustomerId())
        .createdAt(LocalDateTime.now())
        .customerBalanceUpdatedAt(customerBalance.getUpdatedAt())
        .build();
  }
}
