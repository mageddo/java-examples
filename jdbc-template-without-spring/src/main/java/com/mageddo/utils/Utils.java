package com.mageddo.utils;

import org.springframework.core.env.Environment;
import org.springframework.profile.SpringEnvSingleton;

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

	public static Environment getEnv(){
		SpringEnvSingleton.prepareEnv(new String[]{});
		return SpringEnvSingleton.getEnv();
	}
}
