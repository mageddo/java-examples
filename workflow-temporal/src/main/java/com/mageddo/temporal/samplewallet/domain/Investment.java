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
@Table(name = "INVESTMENT")
public class Investment {

  @Id
  @NonNull
  @Column(name = "IDT_INVESTMENT", nullable = false)
  String id;

  @NonNull
  @Column(name = "IDT_WALLET", nullable = false)
  String walletId;

  @NonNull
  @Column(name = "IDT_INVESTOR", nullable = false)
  String investorId;

  @NonNull
  @Column(name = "IDT_BASE_INVESTMENT", nullable = false)
  String baseInvestmentId;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "IND_PROFILE", nullable = false)
  InvestorProfile profile;

  @Column(name = "FLG_CREATED", nullable = false)
  boolean created;
}
