package com.mageddo.jpa.dao;

import com.mageddo.jpa.entity.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.IntegratedTest;

@IntegratedTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonDAOHSQLDBTest {

	@Autowired
	private PersonDAO personDAO;

	@Test
	public void saveAndFindTest() throws Exception {
		final Person insertedPerson = personDAO.save(new Person("Elvis"));
		final Person foundUser = personDAO.find(insertedPerson.getId());
		Assert.assertEquals(1, foundUser.getId());
	}
}
