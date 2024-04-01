package com.mageddo.itext.pdftextextraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;

import static com.mageddo.itext.pdftextextraction.UpperCaseWordsExtractor.extractUppercaseWordsSentenceFromText;

public class UppercasePhraseExtractorApp {

  public static void main(String[] args) throws IOException {
    final var fileName = "/home/typer/Downloads/Eric Evans 2003 - Domain-Driven Design - Tackling"
        + " Complexity in the Heart of Software.pdf";
    extractUppercaseWordSequencesFromPdf(fileName);
  }

  private static void extractUppercaseWordSequencesFromPdf(final String fileName) throws IOException {

    final var document = new PdfDocument(new PdfReader(fileName));
    try {
      final var sentences = extractUppercaseWordSequencesFromPdf(document);
      System.out.println(new LinkedHashSet<>(sentences));
    } finally {
      document.close();
    }
  }

  private static List<String> extractUppercaseWordSequencesFromPdf(
      PdfDocument document
  ) {
    final var sentences = new ArrayList<String>();
    for (int i = 1; i <= document.getNumberOfPages(); i++) {
      // se nao criar o strategy dentro da interação ele acumula as palavras de todas as páginas
      // executadas no retorno do texto
      final var strategy = new SimpleTextExtractionStrategy();
      final var text = PdfTextExtractor.getTextFromPage(document.getPage(i), strategy);
      sentences.addAll(extractUppercaseWordsSentenceFromText(text));
//      final var s = extractUppercaseWordsSentenceFromText(text);
//      System.out.println("--------------------------------------");
//      System.out.println(s);
//      System.out.println("page: " + i);
//      System.out.println("=====================================");

    }
    return sentences;
  }

}
