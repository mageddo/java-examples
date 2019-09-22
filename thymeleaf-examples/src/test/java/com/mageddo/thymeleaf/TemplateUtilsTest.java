package com.mageddo.thymeleaf;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemplateUtilsTest {

	@Test
	public void processHMTLTemplate() throws Exception {

		Context context = new Context();
		context.setVariable("name", "World");

		String out = TemplateUtils.process("/templates/index.html", context);

		assertTrue(out.contains("<p>World</p>"));
	}
}
