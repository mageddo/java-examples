package com.mageddo.tests;

import com.mageddo.okhttp.OkHttpContributorsTest;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class Utils {
	public static String readResourceAsString(String path) {
		try {
			return IOUtils.toString(OkHttpContributorsTest.class.getResourceAsStream(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
