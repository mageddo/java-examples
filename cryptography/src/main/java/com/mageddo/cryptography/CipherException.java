package com.mageddo.cryptography;

public class CipherException extends RuntimeException {

	public CipherException(Throwable cause) {
		super(cause);
	}

	public CipherException(String message, Throwable cause) {
		super(message, cause);
	}
}
