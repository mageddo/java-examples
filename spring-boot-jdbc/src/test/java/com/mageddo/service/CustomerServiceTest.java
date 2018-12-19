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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceTest {


	@Autowired
	private CustomerService customerService;

	@Autowired
	private DatabaseConfigurationDAO databaseConfigurationDAO;

	@After
	public void reset(){
		databaseConfigurationDAO.resetDatabase();
	}

	/**
	 * Exemplo de saque de forma serial em que desde que a logica de negocio esteja correta o
	 * saque então acontecerá de forma normal e esperada
	 * @throws Exception
	 */
	@Test
	public void updateCustomerBalanceDefaultFlowSuccess() throws Exception {

		final CustomerEntity customer = new CustomerEntity("Mary", "Santos");
		customerService.createCustomer(customer);

		customerService.doCustomerBalanceTurnover(customer.getId(), 50);
		assertEquals((Double)50D, customerService.findCustomerById(customer.getId()).getBalance());
		customerService.doCustomerBalanceTurnover(customer.getId(), -3.00);
		assertEquals((Double)47.0, customerService.findCustomerById(customer.getId()).getBalance());

	}

	/**
	 * Exemplo classico de saque com concorrencia em que esta está sendo tratada na base
	 * impedindo com successo que seja sacado mais do que o cliente tem de saldo, todavia,
	 * na consulta de saldo se tem um problema de status trazendo ainda o saldo antigo enquanto o primeiro saque
	 * não se completa, ao passo que dependendo do caso a consulta deveria esperar o saque terminar
	 * @throws Exception
	 */
	@Test
	public void updateCustomerBalanceConcurrency() throws Exception {

		final CustomerEntity customer = new CustomerEntity("Mary", "Santos");
		customerService.createCustomer(customer);
		customerService.doCustomerBalanceTurnover(customer.getId(), 50);

		final Thread t1 = new Thread(() -> {
			try {
				boolean ok = customerService.updateCustomerBalanceWithSleep(customer.getId(), -50, 0, 3000);
				Assert.assertTrue("", ok);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t1.start();
		Thread.sleep(1000);

		assertEquals(new Double(50.0), customerService.findCustomerById(customer.getId()).getBalance());

		// Não consegue sacar pois a transação anterior tirou todo o dinheiro
		// perceba que a consulta anterior pegou o valor errado mas o saque nao falhar por ser todo feito me base
		final boolean ok = customerService.doCustomerBalanceTurnover(customer.getId(), -3.00);
		Assert.assertFalse(ok);

		t1.join();
		assertEquals(new Double(0.0), customerService.findCustomerById(customer.getId()).getBalance());

	}

}
