package com.mageddo.bank.moneytransference.service;

import com.mageddo.bank.moneytransference.entity.Account;

import java.math.BigDecimal;

public interface AccountBalanceUpdater {
	/**
	 * must subtract the debtor and credit the creditor by the transference amount
	 */
	void updateBalance(Account account, BigDecimal amount);
}
