package com.mageddo.bank.moneytransference.service;

import com.mageddo.bank.moneytransference.entity.Transference;
import com.mageddo.bank.moneytransference.entity.TransferenceStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransferenceMakerService {

	private final AccountBalanceUpdater accountBalanceUpdater;
	private final AccountStatementCreator accountStatementCreator;

	@Transactional
	public void doTransfer(Transference transference){

		TransferenceValidator.validate(transference);

		accountBalanceUpdater.updateBalance(transference.getDebtor(), transference.getAmount().negate());
		accountBalanceUpdater.updateBalance(transference.getCreditor(), transference.getAmount());

		accountStatementCreator.create(TransferenceStatement
			.builder()
			.account(transference.getDebtor())
			.description(transference.getDescription())
			.amount(transference.getAmount().negate())
			.build()
		);

		accountStatementCreator.create(TransferenceStatement
			.builder()
			.account(transference.getCreditor())
			.description(transference.getDescription())
			.amount(transference.getAmount())
			.build()
		);

	}
}
