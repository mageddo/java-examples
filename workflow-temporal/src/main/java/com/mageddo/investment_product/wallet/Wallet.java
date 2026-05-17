package com.mageddo.investment_product.wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(name = "WALLET", schema = "INV")
public class Wallet {

  @Id
  @NonNull
  @Column(name = "IDT_WALLET", nullable = false)
  String id;

  @NonNull
  @Column(name = "IDT_INVESTOR", nullable = false)
  String investorId;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "IND_STATUS", nullable = false)
  WalletStatus status;

  @NonNull
  @Column(name = "DAT_CREATED", nullable = false)
  Instant createdAt;

  @Column(name = "DAT_READY")
  Instant readyAt;

  @Column(name = "DAT_ABORTED")
  Instant abortedAt;
}
