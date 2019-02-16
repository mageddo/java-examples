package com.mageddo.resteasy;

import com.mageddo.imgur.ImageUploadReq;
import com.mageddo.imgur.ImgurService;
import com.mageddo.resteasy.testing.InMemoryRestServer;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.junit.Assert.assertEquals;

public class ImgurServiceTest {

	@ClassRule
	public static final InMemoryRestServer server = new InMemoryRestServer(Proxy.class);

	@Test
	public void uploadImage() {

		// arrange
		final WebTarget target = RestEasy.newClient(1)
			.target(server.getURL());
		final ImgurService imgurService = new ImgurService(target);

		final ImageUploadReq image = new ImageUploadReq()
			.setDescription("Akatsuki Sasori Death")
			.setName("sasori.png")
			.setTitle("Akatsuki Sasori")
			.setIn(getClass().getResourceAsStream("/sasori-death.jpg"))
		;

		// act
		final var imageUploadRes = imgurService.uploadImage(image);

		// assert
		assertEquals("http://i.imgur.com/orunSTu.gif", imageUploadRes.getLink());

	}


	@Path("/")
	public static class Proxy {

		@POST
		@Path(ImgurService.IMAGE_PATH)
		@Consumes(MediaType.MULTIPART_FORM_DATA)
		@Produces(MediaType.APPLICATION_JSON)
		public Response put(MultipartFormDataInput input) throws IOException {
			assertEquals("Akatsuki Sasori", input.getFormDataPart("title", String.class, null));
			assertEquals("28d5bd1edef7e7f230bbf1b162bb1036", md5Hex(input.getFormDataPart("image", InputStream.class, null)));
			return Response.ok(IOUtils.toString(getClass().getResourceAsStream("/image-creation-res.json"))).build();
		}

	}
}
