package com.mageddo.itext.pdftextextraction;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

public class UppercasePhraseExtractorApp {

  public static void main(String[] args) throws IOException {
    final var fileName = "/home/typer/Downloads/Eric Evans 2003 - Domain-Driven Design - Tackling"
        + " Complexity in the Heart of Software.pdf";
    extractUppercaseWordSequencesFromPdf(fileName);
  }

  private static void extractUppercaseWordSequencesFromPdf(final String fileName) throws IOException {
    final var reader = new PdfReader(fileName);
    final var parser = new PdfReaderContentParser(reader);
    final var strategy = new SimpleTextExtractionStrategy();
    try {
      extractUppercaseWordSequencesFromPdf(reader, parser, strategy);
    } finally {
      reader.close();
    }
  }

  private static void extractUppercaseWordSequencesFromPdf(
      PdfReader reader,
      PdfReaderContentParser parser,
      SimpleTextExtractionStrategy strategy
  ) throws IOException {
    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
      parser.processContent(i, strategy);
      System.out.println(strategy.getResultantText());

    }
  }

}
