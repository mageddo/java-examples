package com.mageddo.investment_product.financial_event_candidate;

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
@Table(name = "FINANCIAL_EVENT_CANDIDATE")
public class FinancialEventCandidate {

  @Id
  @NonNull
  @Column(name = "IDT_FINANCIAL_EVENT_CANDIDATE", nullable = false)
  String id;

  @NonNull
  @Column(name = "IDT_INVESTMENT", nullable = false)
  String investmentId;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(name = "IND_STATUS", nullable = false)
  CandidateStatus status;

  @Column(name = "FLG_PROCESSED", nullable = false)
  boolean processed;

  @Column(name = "NUM_ATTEMPTS", nullable = false)
  int attempts;

  @Column(name = "DAT_PROCESSED")
  Instant processedAt;
}
