package com.exame;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LocalDriverCreator {

  public static AutoCloseable toAutoCloseable(WebDriver webDriver) {
    return webDriver::close;
  }

  public static WebDriver getInstance() {
    return getInstance("chrome-profile");
  }

  public static WebDriver getInstance(String profile) {

    WebDriverManager.chromedriver().setup();

    final var options = new ChromeOptions();
    options.addArguments(
        String.format("--user-data-dir=%s", createSeleniumUserDataDir(profile)),
        "--lang=en_US"
    );
    final var driver = new ChromeDriver(options);
    driver.manage()
        .timeouts()
        .implicitlyWait(Duration.ofMillis(250));
    return driver;
  }

  private static Path createSeleniumUserDataDir(String profile) {
    try {
      final var path = Paths.get(
          System.getProperty("user.home"),
          ".cache",
          "selenium",
          profile
      );
      Files.createDirectories(path);
      return path;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
