package com.mageddo.jpa.lazycolumnload.service;

import com.mageddo.jpa.lazycolumnload.dao.PersonDAO;
import com.mageddo.jpa.lazycolumnload.entity.Person;
import org.hibernate.cache.internal.NoCachingRegionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	@Autowired
	private PersonDAO personDAO;

	@Autowired
	private PersonService personService;


	@Transactional
	public void createAndFind() {
		int id = personService.create();
		Person foundPerson = personDAO.find(id);
		System.out.println(foundPerson.getName());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int create() {
		return personDAO.save(new Person("Elvis")).getId();
	}
}
