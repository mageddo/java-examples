package com.mageddo.bank.moneytransference.service;

import com.mageddo.bank.moneytransference.entity.TransferenceStatement;

public interface AccountStatementCreator {
	/**
	 * Creates a statement for the transference so the customers can
	 * check de details on some screen later
	 */
	void create(TransferenceStatement transferenceStatement);
}
