package com.mageddo.utils;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

public final class TemplateUtils {

	private TemplateUtils() {
	}

	public static String processFromPath(String templateName, Map<String, Object> variables) {
		return processFromPath(templateName, new Context(Locale.getDefault(), variables));
	}

	public static String processFromPath(String templateName, Context context) {
		try {
			return process(IOUtils.toString(
				TemplateUtils.class.getResourceAsStream(templateName),
				Charset.defaultCharset()
			), context);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static String process(String templateName, Map<String, Object> variables) {
		return process(templateName, new Context(Locale.getDefault(), variables));
	}

	public static String process(String template, Context context){
		TemplateEngine templateEngine = new TemplateEngine();
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateEngine.setTemplateResolver(templateResolver);
		StringWriter stringWriter = new StringWriter();
		templateEngine.process(template, context, stringWriter);
		return stringWriter.toString();
	}
}
