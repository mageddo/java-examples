package com.mageddo.sqldatapartitioning.controller.converter;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.expression.ParseException;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Component
public class LocalDateTimeAnnotationFormatterFactory
	extends EmbeddedValueResolutionSupport
	implements AnnotationFormatterFactory<LocalDateTimeConverter> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		return Set.of(LocalDateTime.class);
	}

	@Override
	public Printer<LocalDateTime> getPrinter(LocalDateTimeConverter annotation, Class<?> fieldType) {
		return createFormatter(annotation);
	}

	@Override
	public Parser<LocalDateTime> getParser(LocalDateTimeConverter annotation, Class<?> fieldType) {
		return createFormatter(annotation);
	}

	private Formatter<LocalDateTime> createFormatter(LocalDateTimeConverter annotation) {
		return new Formatter<>() {
			@Override
			public String print(LocalDateTime o, Locale locale) {
				return String.valueOf(o);
			}
			@Override
			public LocalDateTime parse(String text, Locale locale) throws ParseException {
				return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(annotation.value()));
			}
		};
	}
}
