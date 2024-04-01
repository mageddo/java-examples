package com.mageddo.itext.pdftextextraction;

import org.junit.jupiter.api.Test;

import static com.mageddo.itext.pdftextextraction.UpperCaseWordsExtractor.extractUppercaseWordsSentenceFromText;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UpperCaseWordsExtractorTest {

  @Test
  void mustExtractUpperCaseWordSentence(){

    // arrange
//    final var str = """
//    The software development community widely acknowledges that DOMAIN MODELING is central to
//    SOFTWARE DESIGN.
//    """;

    final var str = """
    The  DOMAIN MODELING x SOFTWARE DESIGN.
    """;

    // act
    final var words = extractUppercaseWordsSentenceFromText(str);

    // assert
    assertNotNull(words);
    assertEquals("""
        [DOMAIN MODELING, SOFTWARE DESIGN]""", words.toString());

  }

}
