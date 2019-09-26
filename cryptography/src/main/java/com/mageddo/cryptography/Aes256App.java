package com.mageddo.cryptography;

import static com.mageddo.cryptography.Aes256.decrypt;
import static com.mageddo.cryptography.Aes256.encrypt;

public class Aes256App {

	public static void main(String[] args) throws Exception {
		final var mySuperSecureKey = "some key";
		final var secureData = encrypt("Hello World".getBytes(), mySuperSecureKey);
		System.out.println(new String(decrypt(secureData, mySuperSecureKey)));
	}
}
