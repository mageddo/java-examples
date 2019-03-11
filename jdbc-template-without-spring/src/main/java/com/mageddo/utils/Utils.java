package com.mageddo.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class Utils {

	private Utils() {
	}

	public static Properties loadProps(){
		try {
			Properties properties = new Properties();
			properties.load(Utils.class.getResourceAsStream("/application.properties"));
			return properties;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
