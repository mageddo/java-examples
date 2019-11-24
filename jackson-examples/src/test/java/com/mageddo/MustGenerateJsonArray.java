package com.mageddo;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MustGenerateJsonArray {

	private SequenceWriter writer;
	private ByteArrayOutputStream bout;

	@BeforeEach
	void before() throws IOException {

		DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(
			"  ", DefaultIndenter.SYS_LF
		);
		DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
		printer.indentObjectsWith(indenter);
		printer.indentArraysWith(indenter);

		this.bout = new ByteArrayOutputStream();
		this.writer = new ObjectMapper()
			.writer(printer)
			.writeValuesAsArray(this.bout)
		;
	}

	@Test
	void mustGenerateJsonArray() throws IOException {

		// arrange

		// act
		this.writer.write("Apple");
		this.writer.write("Banana");
		this.writer.write("Orange");
		this.writer.close();

		// assert
		assertEquals("[\n" +
			"  \"Apple\",\n" +
			"  \"Banana\",\n" +
			"  \"Orange\"\n" +
			"]", this.bout.toString());


	}


}
