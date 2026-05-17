package com.mageddo.temporal.samplewallet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "investment")
public class Investment {

  @Id
  @NonNull
  @Column(name = "id", nullable = false)
  String id;

  @NonNull
  @Column(name = "wallet_id", nullable = false)
  String walletId;

  @NonNull
  @Column(name = "investor_id", nullable = false)
  String investorId;

  @NonNull
  @Column(name = "base_investment_id", nullable = false)
  String baseInvestmentId;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "profile", nullable = false)
  InvestorProfile profile;

  @Column(name = "created", nullable = false)
  boolean created;
}
