package com.mageddo.micronaut.openapi;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title = "Mageddo APP",
        version = "1.0.0",
        description = "Mageddo APP Apis",
        license = @License(
            name = "All the rights are reserved to Mageddo", url = "http://mageddo.com"
        ),
        contact = @Contact(
            url = "http://mageddo.com", name = "Fred", email = "edigitalb@gmail.com"
        )
    )
)
public class MicronautOpenApiStarter {
  public static void main(String[] args) {
    System.setProperty("micronaut.openapi.views.spec", "redoc.enabled=true,rapidoc.enabled=true," +
        "swagger-ui.enabled=true,swagger-ui.theme=flattop");
    Micronaut.run(MicronautOpenApiStarter.class);
  }
}
