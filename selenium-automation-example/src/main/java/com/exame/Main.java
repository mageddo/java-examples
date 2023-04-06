package com.exame;

import org.openqa.selenium.WebDriver;

public class Main {
  public static void main(String[] args) {
    WebDriver instance = LocalDriverCreator.getInstance();
    try {
      instance.get("https://google.com");
    } finally {
      instance.close();
      instance.quit();
    }
  }
}
