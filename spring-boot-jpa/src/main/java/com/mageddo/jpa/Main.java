package com.mageddo.jpa;

import com.mageddo.jpa.entity.Person;
import com.mageddo.jpa.service.PersonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

/**
 * @author elvis
 * @version $Revision: $<br/>
 * $Id: $
 * @since 8/30/17 2:53 PM
 */

@EnableScheduling
@SpringBootApplication
public class Main {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonService personService;

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Scheduled(cron = "0/5 * * * * *")
  public void run() {
    final Person person = personService.save(new Person("Elvis"));
    final Person foundPerson = personService.find(person.getId());
    log.info("status=ran, person={}", foundPerson.getId());
  }
}
