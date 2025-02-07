package com.exame;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteDriverCreator {

  public static AutoCloseable toAutoCloseable(WebDriver webDriver) {
    return webDriver::close;
  }

  public static WebDriver getInstance() {

    final var options = new ChromeOptions();
    options.addArguments("--lang=en_US");
//        String.format("--user-data-dir=%s", createSeleniumUserDataDir(profile)),
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");

    final var driver = new RemoteWebDriver(createUrl("http://localhost:4444"), options);
    driver.manage()
        .timeouts()
        .implicitlyWait(Duration.ofMillis(250));
    return driver;
  }

  private static URL createUrl(final String url) {
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

}
