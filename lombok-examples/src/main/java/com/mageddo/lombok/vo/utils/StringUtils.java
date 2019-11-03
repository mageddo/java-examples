package com.mageddo.lombok.vo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
	public boolean equals(String a, String b){
		return (a == null) || (a.equals(b));
	}
}
