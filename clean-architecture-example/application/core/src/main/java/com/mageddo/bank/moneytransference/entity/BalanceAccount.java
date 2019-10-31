package com.mageddo.bank.moneytransference.entity;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class BalanceAccount {
	private BigDecimal balance;
	private Account account;
}
