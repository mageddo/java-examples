package com.mageddo.cryptography;

import org.junit.jupiter.api.Test;

import static com.mageddo.cryptography.Aes256.decrypt;
import static com.mageddo.cryptography.Aes256.encrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Aes256Test {
	@Test
	public void encryptAndDecrypt(){

		// arrange
		final var mySuperSecureKey = "some key";
		final var text = "Hello World";

		// act
		final var encrypted = encrypt(text.getBytes(), mySuperSecureKey);
		final var decrypted = decrypt(encrypted, mySuperSecureKey);

		// assert
		assertEquals(text, new String(decrypted));

	}
}
