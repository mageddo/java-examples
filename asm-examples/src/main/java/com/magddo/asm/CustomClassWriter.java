package com.magddo.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class CustomClassWriter {

	private AddFieldAdapter addFieldAdapter;
	private PublicizeMethodAdapter pubMethAdapter;
	static String className = Person.class.getName();
	ClassReader reader;
	ClassWriter writer;

	public CustomClassWriter() throws IOException {
		reader = new ClassReader(className);
		writer = new ClassWriter(reader, 0);
	}

	public byte[] addField() {
		addFieldAdapter = new AddFieldAdapter("aNewBooleanField", ACC_PUBLIC, writer);
		reader.accept(addFieldAdapter, 0);
		return writer.toByteArray();
	}

//	public byte[] publicizeMethod() {
//		pubMethAdapter = new PublicizeMethodAdapter(writer);
//		reader.accept(pubMethAdapter, 0);
//		return writer.toByteArray();
//	}
}
