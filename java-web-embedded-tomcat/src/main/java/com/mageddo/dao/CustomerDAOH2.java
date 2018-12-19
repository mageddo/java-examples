package com.mageddo.dao;

import com.mageddo.entity.CustomerEntity;
import com.mageddo.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by elvis on 13/08/16.
 */


public class CustomerDAOH2 implements CustomerDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDAOH2.class);

	private JdbcTemplate jdbcTemplate = DBUtils.getTemplate();

	@Override
	public List<CustomerEntity> findByName(String name) {
		LOGGER.info("status=begin,name={}",  name);
		final List<CustomerEntity> customerEntities = jdbcTemplate.query(
			"SELECT id, first_name, last_name, balance FROM customers WHERE CONCAT(first_name, ' ', last_name) LIKE ? ORDER BY id ASC",
			new Object[]{"%" + name + "%"}, CustomerEntity.mapper()
		);
		LOGGER.info("status=success");
		return customerEntities;
	}

	@Override
	public void create(CustomerEntity customerEntity) {

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			final PreparedStatement st = con.prepareStatement(
			"INSERT INTO customers (first_name, last_name, balance) VALUES (?, ?, 0)", new String[]{"id"}
			);
			st.setString(1, customerEntity.getFirstName());
			st.setString(2, customerEntity.getLastName());
			return st;
		}, keyHolder);
		customerEntity.setId(keyHolder.getKey().longValue());
	}

	@Override
	public void update(CustomerEntity customerEntity) {
		jdbcTemplate.update("UPDATE customers SET first_name=?, last_name=? WHERE id = ?",
				customerEntity.getFirstName(), customerEntity.getLastName(), customerEntity.getId());
	}

	@Override
	public boolean updateCustomerBalanceTurnoverAtDB(Long customerId, double turnoverValue) {
		return jdbcTemplate.update(
			String.format("UPDATE customers SET balance=balance %+.2f WHERE id = ? AND balance %+.2f >= 0.0", turnoverValue, turnoverValue),
			customerId
		) > 0;
	}

	@Override
	public CustomerEntity findCustomerById(Long customerId) {
		return jdbcTemplate.queryForObject("SELECT * FROM customers WHERE id = ?", CustomerEntity.mapper(), customerId);
	}

	@Override
	public boolean updateCustomerBalance(Long customerId, double newBalance) {
		return jdbcTemplate.update("UPDATE customers SET balance=? WHERE id = ?", newBalance, customerId) > 0;
	}
}
