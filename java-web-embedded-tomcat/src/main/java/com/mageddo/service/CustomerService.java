package com.mageddo.service;

import com.mageddo.dao.CustomerDAO;
import com.mageddo.dao.CustomerDAOH2;
import com.mageddo.entity.CustomerEntity;
import com.mageddo.utils.DBUtils;
import com.mageddo.utils.DefaultTransactionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Created by elvis on 13/08/16.
 */
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private CustomerDAO customerDAO = new CustomerDAOH2();
	private PlatformTransactionManager txManger = DBUtils.getTx();

	public List<CustomerEntity> findByName(final String name){
		return new TransactionTemplate(txManger, new DefaultTransactionDefinition())
				.execute(ts -> this.customerDAO.findByName(name) );
	}

	public void createCustomer(CustomerEntity customer) {

		final TransactionTemplate template = new TransactionTemplate(txManger, new DefaultTransactionDefinition());
		template.execute(ts -> {
			customerDAO.create(customer);
			return null;
		});

	}

	public boolean updateCustomerBalanceTurnoverAtDB(Long customerId, double turnoverValue) {

		final TransactionTemplate template = new TransactionTemplate(txManger, new DefaultTransactionDefinition());
		return template.execute(ts -> {
			return customerDAO.updateCustomerBalanceTurnoverAtDB(customerId, turnoverValue);
		});

	}

}
