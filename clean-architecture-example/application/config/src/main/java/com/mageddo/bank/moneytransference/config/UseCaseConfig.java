package com.mageddo.bank.moneytransference.config;

import com.mageddo.bank.moneytransference.service.AccountBalanceUpdater;
import com.mageddo.bank.moneytransference.service.AccountStatementCreator;
import com.mageddo.bank.moneytransference.service.TransferenceMakerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

	@Bean
	public TransferenceMakerService transferenceMakerService(
		AccountBalanceUpdater accountBalanceUpdater, AccountStatementCreator accountStatementCreator
	){
		return new TransferenceMakerService(accountBalanceUpdater, accountStatementCreator);
	}
}
