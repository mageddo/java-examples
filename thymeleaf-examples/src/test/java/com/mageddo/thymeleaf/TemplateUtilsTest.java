package com.mageddo.thymeleaf;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemplateUtilsTest {

  @Test
  public void processHMTLTemplate() throws Exception {

    // arrange
    final Context context = new Context();
    context.setVariable("name", "World");

    // act
    final String out = Thymeleafs.processFromPath("/templates/index.html", context);

    // assert
    assertTrue(out.contains("<p>World</p>"), out);
  }
}
