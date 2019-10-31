package com.mageddo.bank.moneytransference.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Transference {

	@NonNull
	private Account debtor;

	@NonNull
	private Account creditor;

	@NonNull
	private BigDecimal amount;

	@NonNull
	private String description;
}
