package com.mageddo.thymeleaf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.thymeleaf.context.Context;

public class ThymeleafMain {
  public static void main(String[] args) throws IOException, NoSuchMethodException {

//    System.out.println(Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/user/index.html"));

    final Context context = new Context();
    context.setVariable("loggedIn", true);
    context.setVariable("username", "Elvis");

    // act
    final Path tmpFile = Files.createTempFile("index", ".html");
    final String renderedHtml = Thymeleafs.fromPath("/templates/user/index.html", context);
    Files.write(tmpFile, renderedHtml.getBytes());
    System.out.printf("html rendered at file://%s%n", tmpFile);
  }
}
