package com.mageddo.thymeleaf;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

public final class TemplatingUtils {

	private TemplatingUtils() {
	}

	public static String processHMTLTemplate(String templateName, Context context) {

		if(templateName.startsWith("/")){
			try {
				templateName = IOUtils.toString(TemplatingUtils.class.getResourceAsStream(templateName));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		TemplateEngine templateEngine = new TemplateEngine();
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateEngine.setTemplateResolver(templateResolver);
		StringWriter stringWriter = new StringWriter();
		templateEngine.process(templateName, context, stringWriter);
		return stringWriter.toString();
	}
}
