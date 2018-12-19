package com.mageddo.service;

import com.mageddo.utils.SpringEnv;
import com.mageddo.utils.SpringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.env.Environment;

/**
 * Created by elvis on 8/5/17.
 */
public class SpringProfileTest {

	@Test
	public void defaultProfileTest(){

		SpringUtils.prepareEnv(new String[]{});
		final Environment env = SpringUtils.getEnv();
		Assert.assertEquals("DEBUG", env.getProperty("logging.level.root"));

	}

	@Test
	public void prodProfileTest(){

		final Environment env = new SpringEnv(new String[]{"--spring-profiles-active=prod"}).getEnv();
		Assert.assertEquals("INFO", env.getProperty("logging.level.root"));

	}

}
