package com.mageddo.jpa;

import com.mageddo.jpa.dao.PersonDAO;

import com.mageddo.jpa.entity.Person;

import com.mageddo.jpa.service.PersonService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author elvis
 * @version $Revision: $<br/>
 * $Id: $
 * @since 8/30/17 2:53 PM
 */

@SpringBootApplication
@Configuration
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Scheduled(cron = "0/5 * * * *")
  public void run(PersonService personService) {
    final Person person = personService.save(new Person("Elvis"));
    System.out.println(personService.find(person.getId()));
  }
}
