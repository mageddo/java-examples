package com.mageddo.service;

import com.mageddo.dao.CustomerDAO;
import com.mageddo.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/26/16 12:18 PM
 */
@Service
public class ManyCustomersService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManyCustomersService.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private PlatformTransactionManager txManager;


	@Transactional
	public void createCustomers(List<CustomerEntity> customerEntities) {
		for (CustomerEntity customerEntity : customerEntities) {
			customerService.createCustomer(customerEntity);
		}
	}

	@Transactional
	public void createCustomersWithoutFail(List<CustomerEntity> customerEntities) {
		for(CustomerEntity customerEntity: customerEntities){
			try {
					customerService.createCustomerWithoutFailNested(customerEntity);
			} catch (final DuplicateKeyException e) {
				LOGGER.warn("status=duplicated, name={}, msg={}", customerEntity.getFirstName(), e.getMessage(), e);
			}
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createCustomersWithoutFailNotTransactional(List<CustomerEntity> customerEntities) {
		for (CustomerEntity customerEntity : customerEntities) {
			try {
				this.createCustomerWithoutFailRequiresNew(customerEntity);
			} catch (final DuplicateKeyException e) {
				LOGGER.warn("status=duplicated, name={}, msg={}", customerEntity.getFirstName(), e.getMessage(), e);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createCustomerWithoutFailRequiresNew(CustomerEntity customer) {
		customerService.createCustomerWithoutFailMandatory(customer);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createCustomersWithoutFailNotTransactionalProxyFix(List<CustomerEntity> customerEntities) {
		for (CustomerEntity customerEntity : customerEntities) {
			try {
				final TransactionTemplate template = new TransactionTemplate(txManager);
				template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				template.execute(status -> {
					this.createCustomerWithoutFailRequiresNew(customerEntity);
					return null;
				});
			} catch (final DuplicateKeyException e) {
				LOGGER.warn("status=duplicated, name={}, msg={}", customerEntity.getFirstName(), e.getMessage(), e);
			}
		}
	}

}
