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
import org.openqa.selenium.remote.RemoteWebDriver;

public class LocalDriverCreator {

  private static RemoteWebDriver driver;

  public static AutoCloseable toAutoCloseable(WebDriver webDriver){
    return webDriver::close;
  }

  public static WebDriver getInstance() {
    return getInstance("chrome-profile");
  }

  public static WebDriver getInstance(String profile) {
    if (driver != null) {
      return driver;
    }
    final var options = new ChromeOptions();
    options.addArguments(
        String.format("--user-data-dir=%s", createSeleniumUserDataDir(profile)),
        "--lang=en_US"
    );
//    System.setProperty("webdriver.chrome.driver", getChromeWebDriver());
    driver = new ChromeDriver(options);
    driver.manage()
        .timeouts()
        .implicitlyWait(Duration.ofMillis(250));
    return driver;
  }

  private static String getChromeWebDriver() {
    final var path = Paths.get(System.getProperty("user.home"), "selenium", "chromedriver");
    if(!Files.exists(path)){
      throw new IllegalStateException(String.format(
          "Instale o chromedriver em %s, baixe em: %s",
          path,
          "https://chromedriver.chromium.org/downloads"
      ));
    }
    if(!Files.isRegularFile(path)){
      throw new IllegalStateException("Este path deveria ser um arquivo do chrome web driver: " + path);
    }
    return String.valueOf(path);
  }

  private static Path createSeleniumUserDataDir(String profile) {
    try {
      final var path = Paths.get(System.getProperty("user.home"), "/selenium/" + profile);
      Files.createDirectories(path);
      return path;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
