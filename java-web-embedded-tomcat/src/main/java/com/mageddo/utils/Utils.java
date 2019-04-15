package com.mageddo.utils;

import com.mageddo.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * Created by elvis on 29/04/17.
 */
public final class Utils {

	private Utils() {
	}

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	public static String getDeployPath() throws URISyntaxException {
		final String deployPath = Application.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		LOG.debug("deploy-path={}", deployPath);
		return deployPath;
	}
}
