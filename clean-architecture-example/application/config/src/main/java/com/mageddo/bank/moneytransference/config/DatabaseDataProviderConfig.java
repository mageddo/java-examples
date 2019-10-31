package com.mageddo.bank.moneytransference.config;

import com.mageddo.bank.moneytransference.dataproviders.AccountDAOH2;
import com.mageddo.bank.moneytransference.dataproviders.StatementDAOH2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class DatabaseDataProviderConfig {

	@Bean
	public AccountDAOH2 accountDAOH2(EntityManager entityManager){
		return new AccountDAOH2(entityManager);
	};

	@Bean
	public StatementDAOH2 statementDAOH2(EntityManager entityManager){
		return new StatementDAOH2(entityManager);
	};
}
