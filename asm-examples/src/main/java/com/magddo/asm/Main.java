package com.magddo.asm;

import java.lang.reflect.Method;

public class Main {

	public static void main(String[] args) throws Exception {
		loadClass(new CustomClassWriter().addField(), Person.class.getName());
	}

	private static Class loadClass(byte[] byteCode, String className) throws Exception {
		final ClassLoader loader = ClassLoader.getSystemClassLoader();
		Method method = Class.forName("java.lang.ClassLoader").getDeclaredMethod(
			"defineClass",
			String.class, byte[].class, int.class, int.class
		);
		method.setAccessible(true);
		try {
			return (Class) method.invoke(loader, className, byteCode, 0, byteCode.length);
		} finally {
			method.setAccessible(false);
		}
	}
}
