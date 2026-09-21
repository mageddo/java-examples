package com.mageddo.ff4j;

import org.ff4j.FF4j;
import org.ff4j.web.FF4jProvider;

public class SpringFf4jProvider implements FF4jProvider {

	public static SpringFf4jProvider getInstance() {
		return new SpringFf4jProvider();
	}

	@Override
	public FF4j getFF4j() {
		return FeatureSwitch.ff4j();
	}
}
