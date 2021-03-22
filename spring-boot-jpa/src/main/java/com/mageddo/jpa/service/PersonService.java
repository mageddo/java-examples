package com.mageddo.jpa.service;

import com.mageddo.jpa.dao.PersonDAO;
import com.mageddo.jpa.entity.Person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Connection;

@Service
public class PersonService {

  private final PersonDAO personDAO;

  @Autowired
  public PersonService(PersonDAO personDAO) {
    this.personDAO = personDAO;
  }

  @Transactional
  public Person save(Person person) {
    final Person saved = this.personDAO.save(person);
    return saved;
  }

  public Person find(int id) {
    return this.personDAO.find(id);
  }
}
