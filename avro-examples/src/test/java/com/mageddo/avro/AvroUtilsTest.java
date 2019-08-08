package com.mageddo.avro;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AvroUtilsTest {

	@Test
	public void serialize() {

		// arrange
		User user = User
			.newBuilder()
			.setName("Elvis")
			.setFavoriteColor("Green")
			.setFavoriteNumber(7)
			.build();

		// act
		final byte[] avroBytes = AvroUtils.serialize(user);

		// assert
		assertNotNull(avroBytes);
	}

	@Test
	public void deserialize() throws Exception {

		// arrange
		byte[] avroBytes = getResourceAsBytes("/avro-utils-test/001.avro");

		// act
		final User user = AvroUtils.deserialize(avroBytes, User.getClassSchema());

		// assert
		assertEquals("Elvis", user.getName());
		assertEquals(Integer.valueOf(7), user.getFavoriteNumber());
		assertEquals("Green", user.getFavoriteColor());
		assertEquals("{\"name\": \"Elvis\", \"favoriteNumber\": 7, \"favoriteColor\": \"Green\"}", user.toString());

	}

	@Test
	public void deserializeWithClass() throws Exception {

		// arrange
		byte[] avroBytes = getResourceAsBytes("/avro-utils-test/001.avro");

		// act
		final User user = AvroUtils.deserialize(avroBytes, User.class);

		// assert
		assertEquals("Elvis", user.getName());
		assertEquals(Integer.valueOf(7), user.getFavoriteNumber());
		assertEquals("Green", user.getFavoriteColor());
		assertEquals("{\"name\": \"Elvis\", \"favoriteNumber\": 7, \"favoriteColor\": \"Green\"}", user.toString());

	}

	private byte[] getResourceAsBytes(String path) throws IOException {
		return IOUtils.toByteArray(getClass().getResourceAsStream(path));
	}
}
