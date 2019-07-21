package com.mageddo.okhttp;

import com.mageddo.okhttp.vo.Contributor;
import com.mageddo.tests.InMemoryRestServer;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.mageddo.tests.Utils.readResourceAsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OkHttpContributorsTest {

	@ClassRule
	public static InMemoryRestServer inMemoryRestServer = new InMemoryRestServer(OkHttpContributorsTest.Proxy.class);

	@Test
	public void shouldGetContributorsList(){

		// arrange
		final MoshiOkHttpContributors moshiOkHttpContributors = new MoshiOkHttpContributors(inMemoryRestServer.getURL());

		// act
		List<Contributor> contributors = moshiOkHttpContributors.findContributors();

		// assert
		assertFalse(contributors.isEmpty());
		assertTrue(contributors.get(0).getContributions() > 0);
	}

	@Path("/repos/square/okhttp/contributors")
	public static class Proxy {

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/")
		public Response getContributors(){
			return Response
				.ok(readResourceAsString("/ok-http-contributors/001.json"), MediaType.APPLICATION_JSON_TYPE)
				.build()
			;
		}
	}
}
