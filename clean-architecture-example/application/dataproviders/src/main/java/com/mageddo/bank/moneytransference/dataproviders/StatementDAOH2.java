package com.mageddo.bank.moneytransference.dataproviders;

import com.mageddo.bank.moneytransference.dataproviders.entity.StatementEntity;
import com.mageddo.bank.moneytransference.entity.TransferenceStatement;
import com.mageddo.bank.moneytransference.service.AccountStatementCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class StatementDAOH2 implements AccountStatementCreator {

	private final EntityManager entityManager;

	@Override
	public void create(TransferenceStatement transferenceStatement) {
		entityManager.persist(StatementEntity.of(transferenceStatement));
	}
}
