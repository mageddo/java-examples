package com.mageddo.jpa.lazycolumnload.dao;

import com.mageddo.jpa.entity.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PersonDAOHSQLDB implements PersonDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Person save(Person person) {
		return entityManager.merge(person);
	}

	@Override
	public Person find(int id) {
		return entityManager.find(Person.class, id);
	}
}
