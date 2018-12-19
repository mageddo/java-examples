package com.mageddo.jaxb;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ReturnFileTest {

	@Test
	public void shouldMapAndParseAllFileProperties() throws Throwable {
		final InputStream in = getClass().getResourceAsStream("/ASLC027_08561701_20181207_20983_ERR.XML");
		final Document returnFile = (Document) JAXBContext
			.newInstance(Document.class)
			.createUnmarshaller()
			.unmarshal(in);

		assertEquals("EGEN0043", returnFile.getReturnFile().getFileName().getErrCode());
		assertEquals("ASLC027_08561701_20181207_20983_ERR", returnFile.getReturnFile().getFileName().getValue());
	}


	public static class ReturnFile {

		private ComplexString fileName;

		@XmlElement(name = "NomArq")
		public ComplexString getFileName() {
			return fileName;
		}

		public ReturnFile setFileName(ComplexString fileName) {
			this.fileName = fileName;
			return this;
		}
	}

	@XmlRootElement(name = "ASLCDOC")
	public static class Document {
		
		private ReturnFile returnFile;

		@XmlElement(name = "BCARQ", required = true)
		public ReturnFile getReturnFile() {
			return returnFile;
		}

		public Document setReturnFile(ReturnFile returnFile) {
			this.returnFile = returnFile;
			return this;
		}
	}
}
