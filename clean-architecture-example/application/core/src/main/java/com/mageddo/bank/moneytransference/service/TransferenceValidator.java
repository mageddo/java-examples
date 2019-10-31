package com.mageddo.bank.moneytransference.service;

import com.mageddo.bank.moneytransference.entity.Transference;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

@UtilityClass
public class TransferenceValidator {

	public void validate(Transference transference){
		Validate.isTrue(
			transference.getAmount().signum() == 1,
			"Amount must be positive"
		);
		Validate.isTrue(
			StringUtils.length(transference.getDescription()) <= 255,
			"Description can't have more than 255 characters"
		);
	}
}
