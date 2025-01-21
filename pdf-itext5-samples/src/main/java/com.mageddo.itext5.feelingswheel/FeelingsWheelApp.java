package com.mageddo.itext.feelingswheel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.itextpdf.text.pdf.Glyph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeelingsWheelApp {

  private static final Logger log = LoggerFactory.getLogger(FeelingsWheelApp.class);

  public static void main(String[] args) throws Exception {
    final var path = Path.of("/home/typer/Downloads/the_feeling_wheel_pdf__allthefeelz_dot_app.pdf");
    final var reader = new PdfReader(Files.newInputStream(path));
    try {

      PdfReader pdfReader = new PdfReader(SOURCE);
      PdfStamper pdfStamper = new PdfStamper(pdfReader, RESULT_STREAM);
      SimpleTextRemover remover = new SimpleTextRemover();

      System.out.printf("\ntest.pdf - Test\n");
      for (int i = 1; i <= pdfReader.getNumberOfPages(); i++)
      {
        System.out.printf("Page %d:\n", i);
        List<List<Glyph>> matches = remover.remove(pdfStamper, i, "Test");
        for (List<Glyph> match : matches) {
          Glyph first = match.get(0);
          Vector baseStart = first.base.getStartPoint();
          Glyph last = match.get(match.size()-1);
          Vector baseEnd = last.base.getEndPoint();
          System.out.printf("  Match from (%3.1f %3.1f) to (%3.1f %3.1f)\n", baseStart.get(I1), baseStart.get(I2), baseEnd.get(I1), baseEnd.get(I2));
        }
      }


    } finally {
      reader.close();
    }
  }
}
