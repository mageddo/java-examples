package com.mageddo.jaxb.writeandparse;

import com.mageddo.jaxb.XMLUtils;
import com.mageddo.jaxb.XMLUtils.XMLValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Files;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.*;

public class XmlValidatorTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void validateXML(){
		XMLValidation validation = XMLUtils.validate(getResource("/xsd/book.xsd"), getResource("/book.xml"));
		assertNull(validation.getError());
		assertTrue(validation.isValid());
	}

	@Test
	public void shouldWriteXMLAndValidateXSD() throws IOException {

		// arrange
		final InputStream xsdIn = getResource("/xsd/book.xsd");
		final File outFile = temporaryFolder.newFile();

		final Book book = new Book()
			.setAuthor("Victor Hugo")
			.setMessageId(new GUID())
			.setTitle("Los Miserables")
			.setPublisher(
				new Publisher()
					.setName("Acme Inc.")
			)
			;
		// act
		XMLUtils.writeXML(book, new FileOutputStream(outFile));
		System.out.println(new String(Files.readAllBytes(outFile.toPath())));
		XMLValidation validation = XMLUtils.validate(xsdIn, new FileInputStream(outFile));

		// assert
		assertTrue(validation.getError() != null ? validation.getError().getMessage() : "", validation.isValid());

	}

	private InputStream getResource(String path) {
		return requireNonNull(getClass().getResourceAsStream(path), "Resource Not found: " + path);
	}
}
