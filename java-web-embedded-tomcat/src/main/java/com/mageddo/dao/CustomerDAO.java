package com.mageddo.dao;

import java.util.List;

import com.mageddo.entity.CustomerEntity;

/**
 * Created by elvis on 13/08/16.
 */
public interface CustomerDAO {

	List<CustomerEntity> findByName(String name);
	void create(CustomerEntity customerEntity);
	void update(CustomerEntity customerEntity);

	/**
	 * Movimenta o saldo do cliente acrescentando ou retirando direto na base para resovler problemas maiores de
	 * concorrencia
	 * @param customerId
	 * @param turnoverValue o valor a ser movimentado negativo ou positivo
	 * @return se movimentou
	 */
	boolean updateCustomerBalanceTurnoverAtDB(Long customerId, double turnoverValue);

	CustomerEntity findCustomerById(Long customerId);

	/**
	 * Apenas seta o valor do novo balanco do usuário
	 * @param customerId
	 * @param newBalance
	 * @return
	 */
	boolean updateCustomerBalance(Long customerId, double newBalance);
}
