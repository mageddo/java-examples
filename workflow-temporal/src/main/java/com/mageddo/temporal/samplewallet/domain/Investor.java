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
@Table(name = "INVESTOR")
public class Investor {

  @Id
  @NonNull
  @Column(name = "IDT_INVESTOR", nullable = false)
  String id;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "IND_PROFILE", nullable = false)
  InvestorProfile profile;
}
