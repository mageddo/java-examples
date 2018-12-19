package com.mageddo.jpa.dao;

import com.mageddo.jpa.entity.Car;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import utils.IntegratedTest;

@IntegratedTest
@RunWith(SpringRunner.class)
public class CarDAOHSQLDBTest {

	@Autowired
	private CarDAO carDAO;

	@Test
	public void saveAndFind() throws Exception {

		final Car savedCar = carDAO.save(new Car("Civic", 2017));
		final Car foundCar = carDAO.find("Civic-2017");

		Assert.assertEquals(savedCar.getId(), foundCar.getId());
	}

}
