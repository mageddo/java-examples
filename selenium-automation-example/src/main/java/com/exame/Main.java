package com.exame;

import org.openqa.selenium.WebDriver;

public class Main {
  public static void main(String[] args) {
    WebDriver instance = RemoteDriverCreator.getInstance();
    try {
      instance.get("https://google.com");
    } finally {
      instance.close();
      instance.quit();
    }
  }
}
