package com.mageddo.sqldatapartitioning.enums;

public enum SkinPriceType {

	RAW(1),
	DAY_SUMMARIZED(2);

	private int code;

	SkinPriceType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
