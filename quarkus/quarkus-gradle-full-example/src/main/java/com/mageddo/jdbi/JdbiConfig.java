package com.mageddo.jdbi;

import javax.enterprise.inject.Produces;

import org.jdbi.v3.core.Jdbi;

public class JdbiConfig {
  @Produces
  public Jdbi jdbi(){
    return Jdbi.create(
        "jdbc:postgresql://localhost:5432/db", "root", "root"
    );
  }
}
