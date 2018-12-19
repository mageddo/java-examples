package com.magedo.gc;

import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		System.out.println("type some key to continue");
		System.in.read();
		readFiles();
		System.out.println("complete, type some key to terminate");
		System.in.read();
	}

	private static void readFiles() throws IOException {
		for (int i = 0; i < 100; i++) {
			final File _200mFile = new File("/tmp/200M.tmp");
			final byte[] bytes = FileUtils.readFileToByteArray(_200mFile);
			System.out.println("file read");
			readFileBytes(bytes);
		}
	}

	private static void readFileBytes(byte[] bytes) throws IOException {
		try(final BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream("/dev/null"))) {
			for (byte b : bytes) {
				fout.write(b);
			}
		}
	}
}
