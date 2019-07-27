package com.mageddo.kafka.utils;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

public class AvroUtils {

	public static byte[] serialize(GenericContainer o){

		final var writer = new GenericDatumWriter<>(o.getSchema());
		final var bout = new ByteArrayOutputStream();
		try {
			final var encoder = EncoderFactory
				.get()
				.binaryEncoder(bout, null);
			writer.write(o, encoder);
			encoder.flush();
			return bout.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	public static <T>T deserialize(byte[] data, Schema schema){
		final var reader = new SpecificDatumReader<>(schema);
		try {
			return (T) reader.read(
				null,
				DecoderFactory
					.get()
					.binaryDecoder(data, null)
			);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
