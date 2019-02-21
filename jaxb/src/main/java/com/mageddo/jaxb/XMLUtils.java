package com.mageddo.jaxb;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class XMLUtils {

	private static final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	public static void writeXML(Object o, OutputStream out) {
		try {
			final Marshaller marshaller = JAXBContext.newInstance(o.getClass()).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.displayName());
			marshaller.marshal(o, out);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public static XMLValidation validate(InputStream xsdIn, InputStream xmlIn) {
		try {
			final Schema schema = schemaFactory.newSchema(new StreamSource(xsdIn));
			final Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlIn));
			return new XMLValidation(true, null);
		} catch (Exception e) {
			return new XMLValidation(false, e);
		}
	}

	public static class XMLValidation {

		private final boolean valid;
		private final Exception error;

		XMLValidation(boolean valid, Exception error) {
			this.valid = valid;
			this.error = error;
		}

		public boolean isValid() {
			return valid;
		}

		public Exception getError() {
			return error;
		}

	}
}
