package com.mageddo.lombok;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.lombok.vo.FruitPrivateConstructor;
import com.mageddo.lombok.vo.FruitPrivateSetters;
import com.mageddo.lombok.vo.FruitSpecifyingBuilder;
import lombok.var;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class JacksonDeserializeImmutableClassesTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void mustDeserializeImmutableClassWithPrivateConstructor() throws Exception {

		// arrange
		final var expectedMethods = new TreeSet<>(Set.of(
			"builder",
			"equals",
			"getName",
			"hashCode",
			"toString"
		));
		final var json = getResourceAsString("/001.json");

		// act
		final var clazz = FruitPrivateConstructor.class;
		final var vo = objectMapper.readValue(json, clazz);

		// assert
		assertEquals("Orange", vo.getName());
		assertEquals(expectedMethods, getClassMethodNames(clazz));
		final var nameField = FieldUtils.getField(clazz, "name", true).getModifiers();
		assertTrue(Modifier.isFinal(nameField));
		assertTrue(Modifier.isPrivate(nameField));

		final var noArgsConstructor = clazz.getDeclaredConstructor().getModifiers();
		assertTrue(Modifier.isPrivate(noArgsConstructor));
		assertEquals(2, clazz.getDeclaredConstructors().length);
	}

	@Test
	public void mustDeserializeImmutableClassWithPrivateSettersAndPublicConstructor() throws Exception {

		// arrange
		final var expectedMethods = new TreeSet<>(Set.of("builder", "getName"));
		final var json = getResourceAsString("/001.json");

		// act
		final var clazz = FruitPrivateSetters.class;
		final var vo = objectMapper.readValue(json, clazz);

		// assert
		assertEquals("Orange", vo.getName());
		assertEquals(expectedMethods, getClassMethodNames(clazz));
		final var nameField = FieldUtils.getField(clazz, "name", true).getModifiers();
		assertFalse(Modifier.isFinal(nameField));
		assertTrue(Modifier.isPrivate(nameField));

		final var noArgsConstructor = clazz.getDeclaredConstructor().getModifiers();
		assertTrue(Modifier.isPublic(noArgsConstructor));
		assertEquals(2, clazz.getDeclaredConstructors().length);

	}

	@Test
	public void mustDeserializeImmutableClassWithBuilder() throws Exception {

		// arrange
		final var expectedMethods = new TreeSet<>(Set.of(
			"getName",
			"builder",
			"equals",
			"hashCode",
			"toString"
		));
		final var json = getResourceAsString("/001.json");
		final var clazz = FruitSpecifyingBuilder.class;

		// act
		final var vo = objectMapper.readValue(json, clazz);

		// assert
		assertEquals("Orange", vo.getName());
		assertEquals(expectedMethods, getClassMethodNames(clazz));
		final var nameField = FieldUtils.getField(clazz, "name", true).getModifiers();
		assertTrue(Modifier.isFinal(nameField));
		assertTrue(Modifier.isPrivate(nameField));

		assertEquals(0, clazz.getConstructors().length);
		try {
			clazz.getDeclaredConstructor();
			Assert.fail("nao deveria ter construtor default");
		} catch (NoSuchMethodException e){}

	}


	private TreeSet<String> getClassMethodNames(Class clazz) {
		return Stream
			.of(clazz.getDeclaredMethods())
			.map(Method::getName)
			.collect(Collectors.toCollection(TreeSet::new));
	}

	private String getResourceAsString(String path){
		try {
			return IOUtils.toString(getClass().getResourceAsStream(path), Charset.defaultCharset());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
