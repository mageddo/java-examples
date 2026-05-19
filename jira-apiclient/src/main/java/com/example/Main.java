package com.example;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class Main {

  public static void main(String[] args) {
    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("https://your-jira-host.atlassian.net/rest/api/3");

    Response res = webTarget
        .path("/issue/{issueKey}")
        .resolveTemplate("issueKey", "PROJ-1")
        .request()
        .header("Authorization", "Bearer YOUR_TOKEN")
        .get();

    try (res) {
      ResponseValidator.success(res);
      System.out.println(res.readEntity(String.class));
    }
  }
}
