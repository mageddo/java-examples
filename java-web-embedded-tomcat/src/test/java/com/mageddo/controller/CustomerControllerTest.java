package com.mageddo.controller;

import com.mageddo.dao.DatabaseBuilderDao;
import com.mageddo.tomcat.TomcatEmbeddedServer;
import org.apache.catalina.startup.Tomcat;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class CustomerControllerTest {

	@Test
	public void shouldReturnCustomerInfo() throws Exception {

		// arrange

		final Tomcat tomcat = new TomcatEmbeddedServer().run();
		final Client client = ResteasyClientBuilder
			.newBuilder()
			.build();

		new DatabaseBuilderDao().buildDatabase();

		// act
		Response res = client
			.target("http://localhost:9095/customer?name=John")
			.request()
			.get()
		;

		// assert
		assertEquals(200, res.getStatus());
		assertTrue(res.readEntity(String.class).contains("John"));

	}

}
