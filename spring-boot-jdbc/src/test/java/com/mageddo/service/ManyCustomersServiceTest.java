package com.mageddo.service;

import com.mageddo.dao.DatabaseConfigurationDAO;
import com.mageddo.entity.CustomerEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.IllegalTransactionStateException;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManyCustomersServiceTest {

	@Autowired
	ManyCustomersService manyCustomersService;

	@Autowired
	CustomerService customerService;

	@Autowired
	private DatabaseConfigurationDAO databaseConfigurationDAO;

	@After
	public void construct() {
		databaseConfigurationDAO.resetDatabase();
	}

	@Test
	public void createCustomers() throws Exception {
		manyCustomersService.createCustomers(Arrays.asList(new CustomerEntity("Elvis", "Souza"),
			new CustomerEntity("Bruna", "Souza")));

		final List<CustomerEntity> users = customerService.findByName("Souza");

		Assert.assertNotNull(users);
		Assert.assertEquals(2, users.size());
		Assert.assertEquals("Elvis", users.get(0).getFirstName());
		Assert.assertEquals("Bruna", users.get(1).getFirstName());
	}

	@Test
	public void createCustomersWithoutFail() throws Exception {

		manyCustomersService.createCustomersWithoutFail(Arrays.asList(new CustomerEntity("Elvis", "Souza"),
			new CustomerEntity("Elvis", "Freitas"), new CustomerEntity("Bruna", "Souza")));

		final List<CustomerEntity> users = customerService.findByName("Souza");

		Assert.assertNotNull(users);
		Assert.assertEquals(2, users.size());
		Assert.assertEquals("Elvis", users.get(0).getFirstName());
		Assert.assertEquals("Souza", users.get(0).getLastName());
		Assert.assertEquals("Bruna", users.get(1).getFirstName());
	}

	/**
	 * Este teste prova que não existe proxy entre os metodos da mesma classe
	 * <p>
	 * Este teste que nao tem transacao chama um metodo NOT_SUPPORTED,
	 * esse metodo chama outro da mesma classe que é REQUIRES_NEW
	 * como não tem proxy ele não cria nova transação, o resultado é que quando é chamado o método de outra classe
	 * que é MANDATORY recebe exceção falando que não tinha transação disponível
	 *
	 * @throws Exception
	 */
	@Test(expected = IllegalTransactionStateException.class)
	public void createCustomersWithoutFailNotTransactionalTest() throws Exception {
		try {
			manyCustomersService.createCustomersWithoutFailNotTransactional(Arrays.asList(new CustomerEntity("Elvis", "Souza")));
		} catch (IllegalTransactionStateException e) {
			Assert.assertEquals(
				"Esperava que não existisse transação pois o proxiamento entre metodos da mesma classe nao funciona no spring",
				"No existing transaction found for transaction marked with propagation 'mandatory'", e.getMessage()
			);
			throw e;
		}
	}

	@Test
	public void createCustomersWithoutFailNotTransactionalFixTest() throws Exception {

		manyCustomersService.createCustomersWithoutFailNotTransactionalProxyFix(
			Arrays.asList(new CustomerEntity("Elvis", "Souza"), new CustomerEntity("Renan", "Martins"),
				new CustomerEntity("Rick", "Ferreira")
			)
		);
		final List<CustomerEntity> users = customerService.findByName("Ferreira");

		Assert.assertNotNull(users);
		Assert.assertEquals(1, users.size());
		Assert.assertEquals("Rick", users.get(0).getFirstName());

	}


}
