package com.mageddo.entity;

import org.springframework.jdbc.core.RowMapper;

/**
 * Created by elvis on 13/08/16.
 */
public class CustomerEntity {

	private long id;
	private String firstName, lastName;
	private Double balance;

	public CustomerEntity() {
	}

	public CustomerEntity(String first_name, String last_name) {
		this(-1L, first_name, last_name);
	}

	public CustomerEntity(long id, String first_name, String last_name) {
		this(id, first_name, last_name, null);
	}

	public CustomerEntity(long id, String first_name, String last_name, Double balance) {
		this.id = id;
		this.firstName = first_name;
		this.lastName = last_name;
		this.balance = balance;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "CustomerEntity{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
	}

	public static RowMapper<CustomerEntity> mapper() {
		return (rs, rowNum) -> new CustomerEntity(
			rs.getLong("id"), rs.getString("first_name"),
			rs.getString("last_name"), rs.getDouble("balance")
		);
	}
}
