package com.mageddo;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = {"classpath:schema.sql", "classpath:balance_test.sql"})
@ComponentScan
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class, NestedRollbackForTest.class })
public class NestedRollbackForTest {

	@Autowired
	private CustomerDAO customerDAO;

	@Test
	public void mustUpdate(){

		// arrange
		final int expectedCustomerId = 1;

		// act
		final int updated = customerDAO.update(expectedCustomerId, 10.0);

		//
		assertEquals(1, updated);
		assertEquals(Double.valueOf("10.00"), customerDAO.findBalance(expectedCustomerId));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Ignore
	public void mustRollbackTransaction(){

		// arrange
		final int expectedCustomerId = 1;
		final Double originalBalance = customerDAO.findBalance(expectedCustomerId);

		// act
		try {
			customerDAO.updateRollback(expectedCustomerId, 10.0);
			fail();
		} catch (Exception e){
			assertEquals("can't update", e.getMessage());
		}

		// assert
		assertEquals(originalBalance, customerDAO.findBalance(expectedCustomerId));
	}

	@Test
	@Ignore
	public void mustThrowExceptionAndDontRollbackTransaction(){

		// arrange
		final int expectedCustomerId = 1;

		// act
		try {
			customerDAO.updateExceptionNoRollback(expectedCustomerId, 10.0);
			fail();
		} catch (Exception e){
			assertEquals("can't update", e.getMessage());
		}

		// assert
		assertEquals(Double.valueOf("50.00"), customerDAO.findBalance(expectedCustomerId));
	}

	@Repository
	public static class CustomerDAO implements InitializingBean {

		@Autowired
		private JdbcTemplate jdbcTemplate;

		@Autowired
		private BeanFactory beanFactory;

		private CustomerDAO customerDAO;

		@Transactional
		public int update(int id, double balance){
			return jdbcTemplate.update("UPDATE CUSTOMER SET BALANCE = ? WHERE ID = ?", balance, id);
		}

		@Transactional
		public int updateRollback(int id, double balance){
			update(id, balance);
			throw new RuntimeException("can't update");
		}

		@Transactional
		public Double findBalance(int id){
			return jdbcTemplate.queryForObject("SELECT BALANCE FROM CUSTOMER WHERE ID = ?", Double.class, id);
		}

		@Transactional
		public void updateExceptionNoRollback(int id, double value) {
			customerDAO.updateExceptionNoRollbackInternal(id, value);
		}

		@Transactional
		public void updateExceptionNoRollbackInternal(int id, double value) {
			update(id, value);
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			customerDAO = beanFactory.getBean(this.getClass());
		}
	}

}

