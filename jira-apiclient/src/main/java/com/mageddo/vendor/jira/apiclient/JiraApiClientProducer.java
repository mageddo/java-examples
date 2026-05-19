package com.mageddo.vendor.jira.apiclient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JiraApiClientProducer {

  @Produces
  @ApplicationScoped
  public MyselfApiClient myselfApiClient() {
    return new MyselfApiClient(ApiClientConfigurer.webTarget());
  }
}
