package com.mageddo.dao;

import com.mageddo.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by elvis on 07/05/17.
 */
public class DatabaseBuilderDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBuilderDao.class);
	private JdbcTemplate template = DBUtils.getTemplate();

	public void buildDatabase() {
		LOGGER.info("status=begin");
		template.execute("DROP TABLE customers IF EXISTS");
		template.execute("CREATE TABLE customers(" +
			"id SERIAL, first_name VARCHAR(255) UNIQUE, last_name VARCHAR(255), balance NUMBER(12, 2))");

		// Split up the array of whole names into an array of first/last names
		List<Object[]> splitUpNames = Arrays.asList("John Woo 1.99", "Jeff Dean 15.0", "Josh Bloch 35.0", "Mark Long 50.50").stream()
			.map(name -> name.split(" "))
			.collect(Collectors.toList());

		// Use a Java 8 stream to print out each tuple of the list
		splitUpNames.forEach(name -> LOGGER.info(String.format("Inserting customer record for %s %s", name[0], name[1], name[2])));

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		template.batchUpdate("INSERT INTO customers(first_name, last_name, balance) VALUES (?,?,?)", splitUpNames);
		LOGGER.info("status=success");
	}

}
