package com.mageddo.jpa.dao;

import com.mageddo.jpa.entity.Car;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/30/17 3:19 PM
 */
@Repository
public class CarDAOHSQLDB implements CarDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Car save(Car car) {
		return entityManager.merge(car);
	}

	@Override
	public Car find(String id) {
		return entityManager.find(Car.class, id);
	}
}
