package com.mageddo.bank.moneytransference.dataproviders;

import com.mageddo.bank.moneytransference.entity.Account;
import com.mageddo.bank.moneytransference.service.AccountBalanceUpdater;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@RequiredArgsConstructor
public class AccountDAOH2 implements AccountBalanceUpdater {

	private final EntityManager entityManager;

	@Override
	public void updateBalance(Account account, BigDecimal amount) {
		final var affected = entityManager.createNativeQuery(
			"UPDATE ACCOUNT SET NUM_BALANCE = NUM_BALANCE + :amount WHERE COD_ACCOUNT = :accountCode AND NUM_BALANCE > :amount"
		)
		.setParameter("amount", amount)
		.setParameter("accountCode", account.getCode())
		.executeUpdate();
		Validate.isTrue(affected == 1, "couldn't update the account balance");
	}
}
