package com.mageddo.imgur;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.mageddo.jackson.JsonUtils;

import org.apache.commons.lang3.Validate;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE;
import static jakarta.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import jakarta.ws.rs.core.Response.Status;

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

    final JsonNode jsonNode = JsonUtils.readTree(webTarget.path(IMAGE_PATH)
        .request(APPLICATION_JSON_TYPE)
        .post(Entity.entity(form, MULTIPART_FORM_DATA_TYPE), InputStream.class));

    Validate.isTrue(jsonNode.at("/status").asInt() == Status.OK.getStatusCode(), "Response must be ok");
    return JsonUtils.readValue(jsonNode.at("/data").traverse(), ImageUploadRes.class);
  }

}
