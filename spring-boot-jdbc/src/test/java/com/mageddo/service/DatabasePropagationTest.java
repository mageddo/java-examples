package com.mageddo.service;

import com.mageddo.dao.DatabaseConfigurationDAO;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabasePropagationTest {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private DatabaseConfigurationDAO databaseConfigurationDAO;

	@After
	public void reset(){
		databaseConfigurationDAO.resetDatabase();
	}

	@Test
	public void mustCreateOneTransactionToEveryRequiredMethodCallGivenCurrentMethodIsNotSupported() {
		customerService.mustCreateCustomerAndCommitGivenActualMethodIsNotSupported();
	}


	@Test
	public void mustCreateOneTransactionToEveryRequiredMethodCallGivenCurrentMethodIsNotSupportedEvenWhenThereIsAlreadyATransactionOpenBefore() {
		customerService.createTransactionAndCallNotSupported();
	}

	@Test
	public void mustCantFindCustomerInNewTransactionGivenCurrentTransactionIsNotCommitedYet(){
		customerService.createAndFindInAnotherTransactionBeforeCommit();
	}
}
