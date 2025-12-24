package com.mageddo.app;

import com.github.scribejava.core.builder.ScopeBuilder;
import com.mageddo.OAuthAuthenticator;
import com.mageddo.ServiceBuilder;

public class GoogleDriveApp {

  public static final String ALL_DRIVE_FILES_READ_AND_WRITE = "https://www.googleapis.com/auth/drive";
  public static final String ALL_DRIVE_FILES_READ = "https://www.googleapis.com/auth/drive.readonly";
  public static final String APP_FILES_ONLY_READ_AND_WRITE = "https://www.googleapis.com/auth/drive.file";

  public static void main(String[] args) {
    final var scope = new ScopeBuilder()
        .withScopes(ALL_DRIVE_FILES_READ_AND_WRITE, ALL_DRIVE_FILES_READ)
        .build();
    final var service = ServiceBuilder.build(scope);

    new OAuthAuthenticator(service).auth();

  }

}
