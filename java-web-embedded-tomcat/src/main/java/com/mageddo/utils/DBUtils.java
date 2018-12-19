package com.mageddo.utils;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
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

	static {

		/**
		 * Reference: https://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html
		 */
		final Properties connProp = new Properties();
		connProp.setProperty("username", "sa");
		connProp.setProperty("password", "");
		connProp.setProperty("url", "jdbc:h2:mem:test");
		connProp.setProperty("driverClassName", "org.h2.Driver");

//		datasource = new SimpleDriverDataSource(new Driver(), "jdbc:h2:tcp://h2.dev:9092/h2/data", connProp);
//		datasource = new SimpleDriverDataSource(new Driver(), "jdbc:h2:mem:test", connProp);

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

}
