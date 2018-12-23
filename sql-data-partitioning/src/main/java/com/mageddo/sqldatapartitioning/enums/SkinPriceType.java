package com.mageddo.sqldatapartitioning.enums;

public enum SkinPriceType {

	RAW(1),
	DAY_SUMMARIZED(2);

	private int code;

	SkinPriceType(int code) {
		this.code = code;
	}

	public static SkinPriceType fromCode(int code) {
		for (SkinPriceType value : values()) {
			if(value.getCode() == code) {
				return value;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}
}
