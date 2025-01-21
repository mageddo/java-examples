package com.mageddo.itext.feelingswheel;

import java.nio.file.Files;
import java.nio.file.Path;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeelingsWheelApp {

  private static final Logger log = LoggerFactory.getLogger(FeelingsWheelApp.class);

  public static void main(String[] args) throws Exception {
    final var path = Path.of("/home/typer/Downloads/the_feeling_wheel_pdf__allthefeelz_dot_app.pdf");
    final var document = new PdfDocument(new PdfReader(Files.newInputStream(path)));
    try {
      final var page = document.getPage(1);
      final var streamsCount = page.getContentStreamCount();
      log.info("streamsCount={}", streamsCount);


//      System.out.println(PdfTextExtractor.getTextFromPage(document.getPage(1),
//          new SimpleTextExtractionStrategy()));

      for (int i = 0; i < streamsCount; i++) {
        final var stream = page.getContentStream(i);
        stream.get(PdfName.Filter)
        stream.entrySet().forEach(entry -> {
          log.info("pdfName={}, value={}", entry.getKey(), entry.getValue());
        });
//        log.info(
//            "status=streamDetail, length={}, content={}",
//            stream.getLength(), new String(stream.getBytes(true))
//        );
//        final var txt = stream.getAsString(PdfName.Contents);
//        log.info("txt={}", txt);
      }

    } finally {
      document.close();
    }
  }
}
