package com.mageddo.utils;

import org.springframework.core.env.Environment;

/**
 * Created by elvis on 07/05/17.
 */
public class SpringUtils {

	private static Environment instance;

	/**
	 * Load spring properties based on active profiles
	 * @param args
	 * @return
	 */
	public static boolean prepareEnv(String[] args){
		if (instance == null) {
			instance = new SpringEnv(args).getEnv();
			return true;
		}
		return false;
	}

	public static Environment getEnv() {
		return instance;
	}
}
