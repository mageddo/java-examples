package com.mageddo.sqldatapartitioning.controller.converter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface LocalDateTimeConverter {
	/**
	 * Date format
	 * @see java.time.format.DateTimeFormatter
	 */
	String value() default "yyy-MM-ddTHH:MM:ss";
}
