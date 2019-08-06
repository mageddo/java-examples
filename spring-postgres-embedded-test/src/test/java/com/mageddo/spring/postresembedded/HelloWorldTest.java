package com.mageddo.spring.postresembedded;


import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@AutoConfigureEmbeddedDatabase
@SpringBootTest
@ContextConfiguration(classes = {
	App.class
})
@RunWith(SpringRunner.class)
public class HelloWorldTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	@FlywayTest
	@Sql("classpath:/db/hello-world-test-001.sql")
	public void shouldFindHelloWorld(){

		// act
		final var rows = jdbcTemplate.queryForList("SELECT * FROM FRUIT");

		// assert
		assertEquals(1, rows.size());
	}
}
