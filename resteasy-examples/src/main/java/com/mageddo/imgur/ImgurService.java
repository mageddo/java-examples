package com.mageddo.imgur;

import com.fasterxml.jackson.databind.JsonNode;
import com.mageddo.jackson.JsonUtils;
import org.apache.commons.lang3.Validate;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;

import static com.mageddo.jackson.JsonUtils.readTree;
import static javax.ws.rs.core.MediaType.*;

public class ImgurService {

	public static final String IMAGE_PATH = "3/image";

	private final WebTarget webTarget;

	public ImgurService(WebTarget webTarget) {
		this.webTarget = webTarget;
	}

	public ImageUploadRes uploadImage(ImageUploadReq imageUpload){

		final var form = new MultipartFormDataOutput();
		form.addFormData("image", imageUpload.getIn(), APPLICATION_OCTET_STREAM_TYPE);
		form.addFormData("type", "file", TEXT_PLAIN_TYPE);
		form.addFormData("name", imageUpload.getName(), TEXT_PLAIN_TYPE);
		form.addFormData("title", imageUpload.getTitle(), TEXT_PLAIN_TYPE);
		form.addFormData("description", imageUpload.getDescription(), TEXT_PLAIN_TYPE);

		final JsonNode jsonNode = readTree(webTarget.path(IMAGE_PATH)
			.request(APPLICATION_JSON_TYPE)
			.post(Entity.entity(form, MULTIPART_FORM_DATA_TYPE), InputStream.class));

		Validate.isTrue(jsonNode.at("/status").asInt() == Status.OK.getStatusCode(), "Response must be ok");
		return JsonUtils.readValue(jsonNode.at("/data").traverse(), ImageUploadRes.class);
	}
}
