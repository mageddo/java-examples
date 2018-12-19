package com.mageddo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by elvis on 07/05/17.
 */
public class SpringEnv {

	private static final Log LOGGER = LogFactory.getLog(SpringUtils.class);

	private final StandardEnvironment env;

	public SpringEnv(final String[] args) {

		LOGGER.debug("status=begin, args=" + Arrays.toString(args));
		env = new StandardEnvironment();

		try {
			final MutablePropertySources propertySources = env.getPropertySources();
			propertySources.addLast(new SimpleCommandLinePropertySource(args));
			addPropertySource(propertySources, "");

			final String activeProfiles = env.getProperty("spring-profiles-active");
			if(activeProfiles != null && !activeProfiles.trim().isEmpty()){
				for (final String profile : activeProfiles.split(", ?")) {
					addPropertySource(propertySources, profile);
				}
			}
			LOGGER.debug("status=success");
		} catch (IOException e) {
			LOGGER.error("status=fail-load-profile, msg=" + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public StandardEnvironment getEnv() {
		return env;
	}

	private void addPropertySource(MutablePropertySources propertySources, String profileName) throws IOException {
		LOGGER.debug("status=begin, profile=" + profileName);
		final String propertiesName = getPropertiesName(profileName);
		final Properties properties = loadProfileProperties(propertiesName);
		if (properties != null) {
			propertySources.addFirst(new PropertiesPropertySource(propertiesName, properties)); ;
		}
		LOGGER.debug("status=begin, profile="+ profileName +", properties=" + propertiesName);
	}

	private Properties loadProfileProperties(String propertiesName) throws IOException {

		final InputStream profileIn = ClassUtils
			.getDefaultClassLoader()
			.getResourceAsStream(propertiesName);

		if (profileIn == null) {
			return null;
		}
		final Properties properties = new Properties();
		properties.load(profileIn);
		return properties;
	}

	private String getPropertiesName(String profileName) {
		return "application" + (StringUtils.isEmpty(profileName) ? "" : "-" + profileName) + ".properties";
	}

}
