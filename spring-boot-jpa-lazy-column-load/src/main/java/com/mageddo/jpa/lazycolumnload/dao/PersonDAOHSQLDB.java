package com.mageddo.jpa.lazycolumnload.dao;

import com.mageddo.jpa.lazycolumnload.entity.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PersonDAOHSQLDB implements PersonDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Person save(Person person) {
		return entityManager.merge(person);
	}

	@Override
	public Person find(int id) {
		return entityManager.createQuery("FROM Person WHERE id=:id", Person.class)
			.setParameter("id", id)
			.getSingleResult();
	}
}
