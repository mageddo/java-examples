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
@Table(name = "financial_event_candidate")
public class FinancialEventCandidate {

  @Id
  @NonNull
  @Column(name = "id", nullable = false)
  String id;

  @NonNull
  @Column(name = "investment_id", nullable = false)
  String investmentId;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  CandidateStatus status;

  @Column(name = "processed", nullable = false)
  boolean processed;

  @Column(name = "attempts", nullable = false)
  int attempts;

  @Column(name = "processed_at")
  Instant processedAt;
}
