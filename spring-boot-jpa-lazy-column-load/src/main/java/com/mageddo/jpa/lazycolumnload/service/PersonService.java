package com.mageddo.jpa.lazycolumnload.service;

import com.mageddo.jpa.lazycolumnload.dao.PersonDAO;
import com.mageddo.jpa.lazycolumnload.entity.Person;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	private final PersonDAO personDAO;

	public PersonService(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@Transactional
	public void createAndFind() {
		final Person person = personDAO.save(new Person("Elvis"));
		personDAO.find(person.getId()).getName();
	}
}
