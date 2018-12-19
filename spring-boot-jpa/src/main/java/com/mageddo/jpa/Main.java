package com.mageddo.jpa;

import com.mageddo.jpa.dao.PersonDAO;
import com.mageddo.jpa.entity.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/30/17 2:53 PM
 */

@SpringBootApplication
@Configuration
public class Main {

	public static void main(String[] args) {

		final ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);

		final PersonDAO personDAO = ctx.getBean(PersonDAO.class);
		final Person person = personDAO.save(new Person("Elvis"));

		System.out.println(personDAO.find(person.getId()));

	}
}
