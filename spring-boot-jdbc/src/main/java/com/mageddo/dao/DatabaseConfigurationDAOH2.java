package com.mageddo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Profile("h2")
@Repository
public class DatabaseConfigurationDAOH2 implements DatabaseConfigurationDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void resetDatabase(){
		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY=0");
		final List<String> query = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> {

			final String tableName = rs.getString("TABLE_NAME");
			jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
			return tableName;

		});

		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY=1");
	}

}
