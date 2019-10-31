package com.mageddo.bank.moneytransference.dataproviders.entity;

import com.mageddo.bank.moneytransference.entity.TransferenceStatement;
import lombok.Builder;
import lombok.Value;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "STATEMENT")
@Value
@Builder
public class StatementEntity {

	@Id
	@GeneratedValue
	@Column(name = "IDT_STATEMENT", updatable = false)
	private Long id;

	@Column(name = "COD_ACCOUNT", updatable = false)
	private String accountCode;

	@Column(name = "NUM_AMOUNT", updatable = false)
	private BigDecimal amount;

	@Column(name = "DES_STATEMENT", updatable = false)
	private String description;

	public static StatementEntity of(TransferenceStatement transferenceStatement) {
		return StatementEntity
			.builder()
			.accountCode(transferenceStatement.getAccount().getCode())
			.description(transferenceStatement.getDescription())
			.amount(transferenceStatement.getAmount())
			.build()
			;
	}
}
