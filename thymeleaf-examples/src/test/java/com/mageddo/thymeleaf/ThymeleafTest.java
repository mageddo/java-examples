package com.mageddo.thymeleaf;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThymeleafTest {

  @Test
  public void processHMTLTemplate() throws Exception {

    // arrange
    final Context context = new Context();
    context.setVariable("name", "Joanna");

    // act
    final String out = Thymeleafs.fromPath("/templates/index.html", context);

    // assert
    assertTrue(out.contains("<p>Joanna</p>"), out);
  }

  @Test
  public void mustRenderPageWithNotLoggedInState() throws Exception {

    // arrange
    final Context context = new Context();
    context.setVariable("loggedIn", true);
    context.setVariable("username", "Elvis");

    // act
    final String out = Thymeleafs.fromPath("/templates/user/index.html", context);

    // assert
    assertTrue(out.contains("username: Elvis | <a href=\"/logout\">logout</a>"), out);
  }

  @Test
  public void mustRenderPageWithLoggedInState() throws Exception {

    // arrange
    final Context context = new Context();

    // act
    final String out = Thymeleafs.fromPath("/templates/user/index.html", context);

    // assert
    assertTrue(out.contains("<h2>You are not logged in</h2>"), out);
  }
}
