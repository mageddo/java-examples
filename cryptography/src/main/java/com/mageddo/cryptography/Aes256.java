package com.mageddo.cryptography;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public final class Aes256 {

	private static final BouncyCastleProvider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();
	public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

	private Aes256() {
	}

	public static byte[] encrypt(byte[] input, String key) {
		return doCipher(Cipher.ENCRYPT_MODE, input, key);
	}

	public static byte[] decrypt(byte[] input, String key) {
		return doCipher(Cipher.DECRYPT_MODE, input, key);
	}

	private static byte[] doCipher(int mode, byte[] input, String key) {
		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			cipher.init(mode, buildKey(key), new IvParameterSpec(new byte[16]));
			return cipher.doFinal(input);
		} catch (
			NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
				IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e
		){
			throw new CipherException(e);
		}
	}

	private static Key buildKey(String password) {
		return new SecretKeySpec(DigestUtils.sha256(password), ALGORITHM);
	}
}
