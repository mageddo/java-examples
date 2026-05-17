package com.mageddo.temporal.samplewallet.domain;

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
@Table(name = "wallet")
public class Wallet {

  @Id
  @NonNull
  @Column(name = "id", nullable = false)
  String id;

  @NonNull
  @Column(name = "investor_id", nullable = false)
  String investorId;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  WalletStatus status;

  @NonNull
  @Column(name = "created_at", nullable = false)
  Instant createdAt;

  @Column(name = "ready_at")
  Instant readyAt;

  @Column(name = "aborted_at")
  Instant abortedAt;
}
