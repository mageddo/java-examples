package com.mageddo.jpa.service;

import com.mageddo.jpa.dao.PersonDAO;
import com.mageddo.jpa.entity.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  private final PersonDAO personDAO;

  @Autowired
  public PersonService(PersonDAO personDAO) {
    this.personDAO = personDAO;
  }

  public Person save(Person person) {
    return this.personDAO.save(person);
  }

  public Person find(int id) {
    return this.personDAO.find(id);
  }
}
