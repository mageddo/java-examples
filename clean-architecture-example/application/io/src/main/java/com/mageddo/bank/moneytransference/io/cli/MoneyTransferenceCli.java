package com.mageddo.bank.moneytransference.io.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mageddo.bank.moneytransference.entity.Account;
import com.mageddo.bank.moneytransference.entity.Transference;
import com.mageddo.bank.moneytransference.service.TransferenceMakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

@Configuration
@RequiredArgsConstructor
public class MoneyTransferenceCli implements CommandLineRunner {

	private final TransferenceMakerService transferenceMakerService;
	private final JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {
		final var scanner = new Scanner(System.in);
		final var transferenceBuilder = Transference.builder();

		System.out.println("what's the transference amount ?");
		transferenceBuilder.amount(new BigDecimal(scanner.nextLine()));

		System.out.println("who's the debtor?");
		transferenceBuilder.debtor(
			Account
				.builder()
				.code(scanner.nextLine())
				.build()
		);

		System.out.println("who's the creditor?");
		transferenceBuilder.creditor(
			Account
				.builder()
				.code(scanner.nextLine())
				.build()
		);

		System.out.println("who's the description?");
		transferenceBuilder.description(scanner.nextLine());

		transferenceMakerService.doTransfer(transferenceBuilder.build());

		final var om = new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.disable(SerializationFeature.CLOSE_CLOSEABLE)
		;
		// don't do that, don't access the database directly, I'm using just for debug
		System.out.println(om.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM ACCOUNT")));
		System.out.println(om.writeValueAsString(jdbcTemplate.queryForList("SELECT * FROM STATEMENT")));

	}
}
