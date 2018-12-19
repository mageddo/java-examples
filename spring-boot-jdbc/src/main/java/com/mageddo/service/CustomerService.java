package com.mageddo.service;

import com.mageddo.dao.CustomerDAO;
import com.mageddo.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.Assert.isTrue;

@Service
@Transactional
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CustomerService customerService;


	@Autowired
	private PlatformTransactionManager txManger;

	public List<CustomerEntity> findByName(String name){
		return this.customerDAO.findByName(name);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void mustCreateCustomerAndCommitGivenActualMethodIsNotSupported() {
		final CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setBalance(10.5);
		customerEntity.setFirstName("Linux");
		customerEntity.setLastName("Torvalds");
		customerService.createCustomer(customerEntity);
		isTrue(customerService.findByNameNewTransaction("Linux").size() > 0);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void createAndFindInAnotherTransactionBeforeCommit(){
		final CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setBalance(10.5);
		customerEntity.setFirstName("Linux");
		customerEntity.setLastName("Torvalds");
		customerService.createCustomer(customerEntity);
		isTrue(customerService.findByNameNewTransaction("Linux").isEmpty());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<CustomerEntity> findByNameNewTransaction(String name) {
		return findByName(name);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void createCustomer(CustomerEntity customer) {
		customerDAO.create(customer);
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = DuplicateKeyException.class)
	public void createCustomerWithoutFail(CustomerEntity customer) {
		customerDAO.create(customer);
	}

	@Transactional(propagation = Propagation.NESTED)
	public void createCustomerWithoutFailNested(CustomerEntity customer) {
		customerDAO.create(customer);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void createCustomerWithoutFailMandatory(CustomerEntity customer) {
		createCustomerWithoutFail(customer);
	}

	@Transactional
	public boolean doCustomerBalanceTurnover(Long customerId, double turnoverValue) {
		LOGGER.info("status=begin, customerId={}", customerId);
		final boolean ok = customerDAO.doCustomerBalanceTurnover(customerId, turnoverValue);
		LOGGER.info("status=success");
		return ok;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public boolean updateCustomerBalanceConcurrencyProblem(Long customerId, double turnoverValue) {
		LOGGER.info("status=begin, customerId={}", customerId);
		final CustomerEntity customer = customerDAO.findCustomerById(customerId);
		final double newBalance;
		if(turnoverValue > 0.0){
			newBalance = customer.getBalance() - turnoverValue;
		}else{
			newBalance = customer.getBalance() + turnoverValue;
		}
		if(newBalance < 0.0){
			throw new IllegalStateException("No balance available");
		}
		final boolean ok = customerDAO.updateCustomerBalance(customerId, newBalance);
		LOGGER.info("status=success, ok={}", ok);
		return ok;
	}

	@Transactional
	public boolean updateCustomerBalanceWithSleep(Long customerId, double turnoverValue, int before, int after) throws InterruptedException {
		LOGGER.info("status=begin, customerId={}", customerId);
		Thread.sleep(before);
		final boolean balanceUpdate = this.doCustomerBalanceTurnover(customerId, turnoverValue);
		Thread.sleep(after);
		LOGGER.info("status=success");
		return balanceUpdate;
	}

	@Transactional
	public CustomerEntity findCustomerById(Long customerId) {
		LOGGER.info("status=begin, customerId={}", customerId);
		final CustomerEntity customerById = customerDAO.findCustomerById(customerId);
		LOGGER.info("status=success, customerId={}, value={}", customerId, customerById.getBalance());
		return customerById;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public CustomerEntity findCustomerByIdSerial(Long customerId) {

//		TransactionTemplate transactionTemplate = new TransactionTemplate(txManger);
//		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
//		LOGGER.info("status=begin, customerId={}", customerId);
//
//		final CustomerEntity customerById = transactionTemplate.execute(status -> {
//			final CustomerEntity customer = customerDAO.findCustomerById(customerId);
//			return customer;
//		});
//		LOGGER.info("status=success, customerId={}, value={}", customerId, customerById.getBalance());
//		return customerById;


		LOGGER.info("status=begin, customerId={}", customerId);
		final CustomerEntity customerById = customerDAO.findCustomerById(customerId);
		LOGGER.info("status=success, customerId={}, value={}", customerId, customerById.getBalance());
		return customerById;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public boolean updateCustomerBalanceConcurrencyProblemWithSleep(long customerId, double turnoverValue,
																																	int before, int after) throws InterruptedException {

		LOGGER.info("status=begin, customerId={}", customerId);
		Thread.sleep(before);
		final boolean balanceUpdate = this.updateCustomerBalanceConcurrencyProblem(customerId, turnoverValue);
		Thread.sleep(after);
		LOGGER.info("status=success");
		return balanceUpdate;
	}

	@Transactional
	public void createTransactionAndCallNotSupported() {
		customerService.mustCreateCustomerAndCommitGivenActualMethodIsNotSupported();
	}
}
