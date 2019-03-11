package com.mageddo.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mageddo.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by elvis on 07/05/17.
 */
public class DatabaseBuilderDAOH2 implements DatabaseBuilderDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBuilderDAOH2.class);
	private final JdbcTemplate template = DBUtils.template();
	private boolean builded = false;

	@Override
	public void buildDatabase() {

		if(builded){
			LOGGER.info("status=already-built");
			return ;
		}

		LOGGER.info("status=begin");

		template.execute("DROP TABLE customers IF EXISTS");
		template.execute("CREATE TABLE customers(" +
			"id SERIAL, first_name VARCHAR(255) UNIQUE, last_name VARCHAR(255), balance NUMBER(12, 2))");

		// Split up the array of whole names into an array of first/last names
		final List<Object[]> splitUpNames = Stream.of(
			"John Woo 1.99", "Jeff Dean 15.0", "Josh Bloch 35.0", "Mark Long 50.50"
		)
		.map(name -> name.split(" "))
		.collect(Collectors.toList());

		// Use a Java 8 stream to print out each tuple of the list
		splitUpNames.forEach(name -> LOGGER.info("Inserting customer record for {}", name));

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		template.batchUpdate("INSERT INTO customers(first_name, last_name, balance) VALUES (?,?,?)", splitUpNames);

		builded = true;

		LOGGER.info("status=success");

	}

}
