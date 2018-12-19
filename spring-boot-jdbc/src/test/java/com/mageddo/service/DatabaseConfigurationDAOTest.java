package com.mageddo.service;

import com.mageddo.dao.DatabaseConfigurationDAO;
import com.mageddo.entity.CustomerEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 11/28/16 2:45 PM
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabaseConfigurationDAOTest {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private DatabaseConfigurationDAO databaseConfigurationDAO;

	@Before
	@After
	public void before(){
		databaseConfigurationDAO.resetDatabase();
	}

	@Test
	public void resetDatabase() throws Exception {

		customerService.createCustomer(new CustomerEntity("Jeff", "Mark"));

		Assert.assertEquals("Jeff", customerService.findByName("Jeff").get(0).getFirstName());
		databaseConfigurationDAO.resetDatabase();
		Assert.assertEquals(0, customerService.findByName("Jeff").size());

	}

}