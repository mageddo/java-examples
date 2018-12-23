package com.mageddo.sqldatapartitioning.controller.converter;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.expression.ParseException;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Component
public class LocalDateAnnotationFormatterFactory
	extends EmbeddedValueResolutionSupport
	implements AnnotationFormatterFactory<LocalDateConverter> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		return Set.of(LocalDate.class);
	}

	@Override
	public Printer<LocalDate> getPrinter(LocalDateConverter annotation, Class<?> fieldType) {
		return createFormatter(annotation);
	}

	@Override
	public Parser<LocalDate> getParser(LocalDateConverter annotation, Class<?> fieldType) {
		return createFormatter(annotation);
	}

	private Formatter<LocalDate> createFormatter(LocalDateConverter annotation) {
		return new Formatter<>() {
			@Override
			public String print(LocalDate o, Locale locale) {
				return String.valueOf(o);
			}
			@Override
			public LocalDate parse(String text, Locale locale) throws ParseException {
				return LocalDate.parse(text, DateTimeFormatter.ofPattern(annotation.value()));
			}
		};
	}
}
