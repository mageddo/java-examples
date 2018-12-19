package com.mageddo.utils;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by elvis on 07/05/17.
 */
public class DBUtils {

	private static final DataSourceTransactionManager TX_MANAGER;
	private static final DataSource DATA_SOURCE;
	private static final JdbcTemplate JDBC_TEMPLATE;
	private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

	static {

		final Environment env = Utils.getEnv();
		/*
		 * Reference: https://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html
		 */
		final String[] allProperties = new DataSourceFactory() {
			String[] getAllProperties() {
				return ALL_PROPERTIES;
			}
		}.getAllProperties();
		// dynamically accessing all JDBC properties
		final Properties connProp = new Properties();
		for (final String key : allProperties) {
			final String springKey = "spring.datasource." + key;
			if(env.containsProperty(springKey)){
				connProp.put(key, env.getProperty(springKey));
				LOGGER.info("status=put, key={}", key);
			}
		}

		try {
			DATA_SOURCE = new DataSourceFactory().createDataSource(connProp);
		} catch (Exception e) {
			throw new RuntimeException("Can not create pool", e);
		}

		TX_MANAGER = new DataSourceTransactionManager(DATA_SOURCE);
		JDBC_TEMPLATE = new JdbcTemplate(DATA_SOURCE);
	}

	public static PlatformTransactionManager getTx(){
		return TX_MANAGER;
	}
	public static JdbcTemplate getTemplate(){
		return JDBC_TEMPLATE;
	}

//	public static DataSource getDataSource() {
//		return DATA_SOURCE;
//	}
}
