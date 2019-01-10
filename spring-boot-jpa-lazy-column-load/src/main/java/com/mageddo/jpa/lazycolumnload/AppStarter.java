package com.mageddo.jpa.lazycolumnload;

import com.mageddo.jpa.entity.Person;
import com.mageddo.jpa.lazycolumnload.dao.PersonDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class AppStarter {
	public static void main(String[] args) {
		final ConfigurableApplicationContext ctx = SpringApplication.run(AppStarter.class, args);
		final PersonDAO personDAO = ctx.getBean(PersonDAO.class);
		final Person person = personDAO.save(new Person("Elvis"));
		personDAO.find(person.getId()).getName();
	}
}
