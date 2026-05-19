package com.mageddo.vendor.jira.apiclient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

public class JiraApiClientProducer {

  @Produces
  @Singleton
  public MyselfApiClient myselfApiClient() {
    return new MyselfApiClient(ApiClientConfigurer.webTarget());
  }
}
