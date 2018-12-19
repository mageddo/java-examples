package com.mageddo.utils;

/**
 * Created by elvis on 29/04/17.
 */
public class Utils {

	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
