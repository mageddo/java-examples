package com.mageddo.shardingsphere.region;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(catalog = "CUSTOMER_REGION")
public class CustomerRegion {

  @Id
  @Column(name = "IDT_CUSTOMER_REGION", nullable = false, unique = true)
  private UUID id;

  @Column(name = "NAM_REGION", nullable = false)
  private String name;

  @Column(name = "COD_CUSTOMER", nullable = false)
  private UUID customerId;

}
