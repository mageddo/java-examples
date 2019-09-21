package com.mageddo.http;

import java.net.URL;

public class VanillaHttpMain {
	public static void main(String[] args) throws Exception {
		final var url = "http://acme.com/";
		try (var in = new URL(url).openConnection().getInputStream()){
			final var buff = new byte[512];
			int read;
			while ((read = in.read(buff)) != -1) {
				System.out.write(buff, 0, read);
			}
		}
	}
}
