package com.mageddo.itext.pdftextextraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

import static com.mageddo.itext.pdftextextraction.UpperCaseWordsExtractor.extractUppercaseWordsSentenceFromText;

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
      final var sentences = extractUppercaseWordSequencesFromPdf(reader, parser, strategy);
      System.out.println(sentences);
    } finally {
      reader.close();
    }
  }

  private static List<String> extractUppercaseWordSequencesFromPdf(
      PdfReader reader,
      PdfReaderContentParser parser,
      SimpleTextExtractionStrategy strategy
  ) throws IOException {
    final var sentences = new ArrayList<String>();
    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
      if(i == 445) {
        parser.processContent(i, strategy);
//      sentences.addAll(extractUppercaseWordsSentenceFromText(strategy.getResultantText()));
        final var s = extractUppercaseWordsSentenceFromText(strategy.getResultantText(), 2);
        System.out.println(s);
        System.out.println("page: " + i);
      }
    }
    return sentences;
  }

}
